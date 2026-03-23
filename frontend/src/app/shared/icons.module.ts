import { NgModule } from '@angular/core';
import { NgIconsModule } from '@ng-icons/core';
import {
  HeroArrowLeft,
  HeroClipboardList,
  HeroCreditCard,
  HeroDocumentAdd,
  HeroMinus,
  HeroPencilAlt,
  HeroPlus,
  HeroRefresh,
  HeroShoppingCart,
  HeroTrash,
  HeroX,
} from '@ng-icons/heroicons';

@NgModule({
  imports: [
    NgIconsModule.withIcons({
      Back: HeroArrowLeft,
      Cart: HeroShoppingCart,
      Checkout: HeroCreditCard,
      Close: HeroX,
      Edit: HeroPencilAlt,
      Minus: HeroMinus,
      NewProduct: HeroDocumentAdd,
      Plus: HeroPlus,
      Products: HeroClipboardList,
      Refresh: HeroRefresh,
      Remove: HeroTrash,
    }),
  ],
  exports: [NgIconsModule],
})
export class IconsModule {}
