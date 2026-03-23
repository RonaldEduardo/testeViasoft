import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IconsModule } from '../../shared/icons.module';

@NgModule({
  declarations: [ProductListComponent, ProductFormComponent],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    IconsModule,
  ],
  exports: [ProductListComponent, ProductFormComponent],
})
export class ProductModule {}
