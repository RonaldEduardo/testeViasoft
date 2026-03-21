import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  filtroNome: string = '';
  filtroCategoria: string = '';

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => (this.products = data),
      error: (err) => console.error('Erro de conexão ou CORS:', err),
    });
  }

  get produtosFiltrados() {
    return this.products.filter((p) => {
      const matchNome =
        !this.filtroNome ||
        p.name.toLowerCase().includes(this.filtroNome.toLowerCase());

      const matchCategoria =
        !this.filtroCategoria || p.category.toString() === this.filtroCategoria;

      return matchNome && matchCategoria;
    });
  }

  limparFiltros() {
    this.filtroNome = '';
    this.filtroCategoria = '';
  }
}
