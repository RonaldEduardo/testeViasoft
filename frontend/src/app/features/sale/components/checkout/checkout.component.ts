import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil, finalize, forkJoin } from 'rxjs';

import { SaleCreateDTO } from '../../dto/sale-create.dto';
import { SaleItemRequestDTO } from '../../dto/sale-item-request.dto';
import { Producer } from '../../models/producer';
import { ProducerService } from '../../services/producer.service';
import { SaleService } from '../../services/sale.service';
import { SaleItemDTO } from '../../dto/sale-item.dto';
import { CartService } from 'src/app/shared/services/cart.service';
import { SaleResponseDTO } from '../../dto/sale-response.dto';
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
  appliedDiscountValue = 0;
  isSubmitting = false;
  errorMessage = '';

  private readonly destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private producerService: ProducerService,
    private saleService: SaleService,
    private cartService: CartService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.loadProducers();

    this.loadCartItems();
  }

  private loadCartItems() {
    this.cartService.items$
      .pipe(takeUntil(this.destroy$))
      .subscribe((items) => {
        this.items = items;
        this.updatePricesFromBackend();
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

  OnSubmit() {
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
      totalValue: this.finalTotalValue,
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
        next: (response) => {
          this.applyBackendCalculation(response);

          this.cartService.clearCart();
          this.router.navigate(['/products']);
        },
        error: (err) => {
          this.errorMessage =
            err?.error?.message ||
            'Não foi possível finalizar a compra. Tente novamente.';
        },
      });
  }

  get finalTotalValue(): number {
    return Math.max(this.totalValue - this.appliedDiscountValue, 0);
  }

  private applyBackendCalculation(response: SaleResponseDTO | void): void {
    if (!response) {
      return;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updatePricesFromBackend() {
    if (this.items.length === 0) {
      this.totalValue = 0;
      this.appliedDiscountValue = 0;
      return;
    }

    const requests = this.items.map((item) =>
      this.saleService.calculateItem({
        productId: item.product.id,
        quantity: item.quantity,
      }),
    );

    forkJoin(requests)
      .pipe(takeUntil(this.destroy$))
      .subscribe((calculatedItems) => {
        let newTotal = 0;
        let newDiscount = 0;

        calculatedItems.forEach((calcInfo, index) => {
          const item = this.items[index];

          item.total = calcInfo.originalUnitPrice! * item.quantity;
          item.product.price = calcInfo.originalUnitPrice!;

          newTotal += item.total;
          newDiscount += (calcInfo.discountValue || 0) * item.quantity;
        });

        this.totalValue = newTotal;
        this.appliedDiscountValue = newDiscount;
      });
  }
}
