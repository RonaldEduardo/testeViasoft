import { SaleItemDTO } from 'src/app/features/sale/dto/saleItemDTO';
import { CartService } from './../../services/cart.service';
import { Component, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-cart-modal',
  templateUrl: './cart-modal.component.html',
})
export class CartModalComponent {
  @Output() close = new EventEmitter<void>();
  @Output() checkout = new EventEmitter<void>();
  items$: Observable<SaleItemDTO[]> = this.cartService.items$;
  total$: Observable<number> = this.cartService.total$;

  constructor(private cartService: CartService) {}

  onClose() {
    this.close.emit();
  }

  onCheckout() {
    this.checkout.emit();
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
}
