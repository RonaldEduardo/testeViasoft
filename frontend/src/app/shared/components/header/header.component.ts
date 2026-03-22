import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
  isModalOpen = false;
  cartCount$ = this.cartService.count$;

  menu = [
    { label: 'Produtos', link: '/products' },
    { label: 'Novo Produto', link: '/products/new' },
    { label: 'Checkout', link: '/checkout' },
  ];

  constructor(
    private cartService: CartService,
    private router: Router,
  ) {}

  toggleModal() {
    this.isModalOpen = !this.isModalOpen;
  }

  goToCheckoutFromModal() {
    this.isModalOpen = false;
    this.router.navigate(['/checkout']);
  }

  ngOnInit(): void {}
}
