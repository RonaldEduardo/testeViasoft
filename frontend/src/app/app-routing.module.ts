import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductFormComponent } from './features/product/components/product-form/product-form.component';
import { ProductListComponent } from './features/product/components/product-list/product-list.component';

const routes: Routes = [
  { path:'', redirectTo: '/products', pathMatch: 'full' },

  { path: 'products', component: ProductListComponent },

  { path: 'products/new', component: ProductFormComponent },

  { path: 'products/edit/:id', component: ProductFormComponent },

  { path: '**', redirectTo: '/products' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
