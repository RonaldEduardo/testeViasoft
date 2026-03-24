import { Product } from '../../product/models/product';

export interface SaleItemDTO {
  product: Product;
  quantity: number;
  total: number;
}
