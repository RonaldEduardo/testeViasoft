import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Category, CategoryLabels } from '../../models/enums/category';
import { Safra, SafraLabels } from '../../models/enums/safra';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
})
export class ProductFormComponent implements OnInit {
  productForm!: FormGroup;
  productId: number | null = null;
  isEditMode = false;

  optionsCategory = Object.entries(CategoryLabels).map(([value, label]) => ({
    value: Number(value),
    label,
  }));

  optionsSafra = Object.entries(SafraLabels).map(([value, label]) => ({
    value: Number(value),
    label,
  }));

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadProductIfEditMode();
  }

  private buildForm(): void {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      category: [null, [Validators.required]],
      price: [null, [Validators.required, Validators.min(0.1)]],
      stockQuantity: [null, [Validators.required, Validators.min(0)]],
      safra: [null, [Validators.required]],
    });
  }

  private loadProductIfEditMode(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) return;

    const id = Number(idParam);
    if (Number.isNaN(id)) return;

    this.productId = id;
    this.isEditMode = true;

    this.mapToForm(id);
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const payload: Product = this.productForm.value;

    if (this.isEditMode && this.productId !== null) {
      this.productService.update(this.productId, payload).subscribe(() => {
        this.router.navigate(['/products']);
      });
      return;
    }

    this.productService.create(payload).subscribe(() => {
      this.router.navigate(['/products']);
    });
  }

  private mapToForm(id: number) {
    this.productService.findById(id).subscribe((product: Product) => {
      const categoryValue =
        typeof product.category === 'string'
          ? Category[product.category as keyof typeof Category]
          : product.category;

      const safraValue =
        typeof product.safra === 'string'
          ? Safra[product.safra as keyof typeof Safra]
          : product.safra;

      this.productForm.patchValue({
        name: product.name,
        category: categoryValue,
        price: product.price,
        stockQuantity: product.stockQuantity,
        safra: safraValue,
      });
    });
  }
}
