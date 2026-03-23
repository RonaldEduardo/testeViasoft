import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil, finalize } from 'rxjs';

import { SaleCreateDTO } from '../../dto/sale-create.dto';
import { SaleItemRequestDTO } from '../../dto/sale-item-request.dto';
import { Producer } from '../../models/producer';
import { ProducerService } from '../../services/producer.service';
import { SaleService } from '../../services/sale.service';
import { SaleItemDTO } from '../../dto/saleItemDTO';
import { CartService } from 'src/app/shared/services/cart.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
})
export class CheckoutComponent implements OnInit, OnDestroy {
  checkoutForm = this.fb.group({
    producerId: [null as number | null, [Validators.required]],
  });

  producers: Producer[] = [];
  items: SaleItemDTO[] = [];
  totalValue = 0;
  isSubmitting = false;
  errorMessage = '';

  private readonly destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private producerService: ProducerService,
    private saleService: SaleService,
    private cartService: CartService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadProducers();

    this.loadCartItems();

    this.updateTotalValueFromCart();
  }

  private updateTotalValueFromCart() {
    this.cartService.total$
      .pipe(takeUntil(this.destroy$))
      .subscribe((total) => {
        this.totalValue = total;
      });
  }

  private loadCartItems() {
    this.cartService.items$
      .pipe(takeUntil(this.destroy$))
      .subscribe((items) => {
        this.items = items;
      });
  }

  private loadProducers() {
    this.producerService
      .getProducers()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (producers) => {
          this.producers = producers;
        },
        error: () => {
          this.errorMessage = 'Nao foi possivel carregar produtores.';
        },
      });
  }

  get totalItemsQuantity(): number {
    return this.items.reduce((acc, curr) => acc + curr.quantity, 0);
  }

  increaseQuantity(item: SaleItemDTO) {
    this.cartService.updateQuantity(item.product.id, item.quantity + 1);
  }

  decreaseQuantity(item: SaleItemDTO) {
    this.cartService.updateQuantity(item.product.id, item.quantity - 1);
  }

  removeItem(item: SaleItemDTO) {
    this.cartService.removeFromCart(item.product.id);
  }

  submitSale() {
    this.errorMessage = '';

    if (this.checkoutForm.invalid) {
      this.checkoutForm.markAllAsTouched();
      return;
    }

    if (!this.items.length || this.totalValue <= 0) {
      this.errorMessage = 'Carrinho vazio.';
      return;
    }

    const mappedItems: SaleItemRequestDTO[] = this.items.map((item) => ({
      productId: item.product.id,
      quantity: item.quantity,
      total: item.total,
    }));

    const payload: SaleCreateDTO = {
      producerId: Number(this.checkoutForm.controls['producerId'].value),
      items: mappedItems,
      totalValue: this.totalValue,
    };

    this.isSubmitting = true;

    this.saleService
      .createSale(payload)
      .pipe(
        finalize(() => {
          this.isSubmitting = false;
        }),
        takeUntil(this.destroy$),
      )
      .subscribe({
        next: () => {
          this.cartService.clearCart();
          this.router.navigate(['/products']);
        },
        error: (err) => {
          this.errorMessage =
            err?.error?.message ||
            'Nao foi possivel finalizar a compra. Tente novamente.';
        },
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
