import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductFormComponent } from './features/product/components/product-form/product-form.component';
import { ProductListComponent } from './features/product/components/product-list/product-list.component';
import { CheckoutComponent } from './features/sale/components/checkout/checkout.component';
import { SalesReportComponent } from './features/sale/components/sales-report/sales-report.component';
import { ProducerFormComponent } from './features/sale/components/producer-form/producer-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },

  { path: 'products', component: ProductListComponent },

  { path: 'checkout', component: CheckoutComponent },

  { path: 'sales', component: SalesReportComponent },

  { path: 'products/new', component: ProductFormComponent },

  { path: 'products/edit/:id', component: ProductFormComponent },

  { path: 'producers/new', component: ProducerFormComponent },

  { path: 'producers/edit/:id', component: ProducerFormComponent },

  { path: '**', redirectTo: '/products' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
