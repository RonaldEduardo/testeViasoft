import { Category } from './enums/category';
import { Safra } from './enums/safra';

export interface Product {
  id: number;
  name: String;
  price: number;
  category: Category;
  stockQuantity: number;
  safra: Safra;
}
