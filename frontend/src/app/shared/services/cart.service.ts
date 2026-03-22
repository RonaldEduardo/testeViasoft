import { Product } from './../../features/product/models/product';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map } from 'rxjs';
import { SaleItemDTO } from 'src/app/features/sale/dto/saleItemDTO';

@Injectable({ providedIn: 'root' })
export class CartService {
  private cartItems = new BehaviorSubject<SaleItemDTO[]>([]);
  items$ = this.cartItems.asObservable();

  count$ = this.items$.pipe(
    map((items) => items.reduce((acc, curr) => acc + curr.quantity, 0)),
  );

  total$ = this.items$.pipe(
    map((items) =>
      items.reduce((acc, curr) => acc + curr.product.price * curr.quantity, 0),
    ),
  );

  addToCart(product: Product) {
    const current = this.cartItems.value;
    const existingIndex = current.findIndex(
      (item) => item.product.id === product.id,
    );

    if (existingIndex >= 0) {
      const updated = [...current];
      updated[existingIndex] = {
        ...updated[existingIndex],
        total:
          updated[existingIndex].product.price *
          (updated[existingIndex].quantity + 1),
        quantity: updated[existingIndex].quantity + 1,
      };

      this.cartItems.next(updated);
      return;
    }

    const newItem: SaleItemDTO = { product, quantity: 1, total: product.price };
    this.cartItems.next([...current, newItem]);
  }

  removeFromCart(productId: number) {
    const filtered = this.cartItems.value.filter(
      (item) => item.product.id !== productId,
    );
    this.cartItems.next(filtered);
  }

  updateQuantity(productId: number, quantity: number) {
    if (quantity <= 0) {
      this.removeFromCart(productId);
      return;
    }

    const updated = this.cartItems.value.map((item) =>
      item.product.id === productId
        ? {
            ...item,
            quantity,
            total: item.product.price * quantity,
          }
        : item,
    );
    this.cartItems.next(updated);
  }

  clearCart() {
    this.cartItems.next([]);
  }
}
