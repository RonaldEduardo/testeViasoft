import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductModule } from './features/product/product.module';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './shared/components/header/header.component';
import { CartModalComponent } from './shared/components/cart-modal/cart-modal.component';
import { CheckoutComponent } from './features/sale/components/checkout/checkout.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IconsModule } from './shared/icons.module';
import { SalesReportComponent } from './features/sale/components/sales-report/sales-report.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CartModalComponent,
    CheckoutComponent,
    SalesReportComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ProductModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    IconsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
