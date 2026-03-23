import { Category } from './enums/category';
import { Safra } from './enums/safra';

export interface Product {
  id: number;
  name: string;
  price: number;
  recipeProduct?: string;
  category: Category;
  stockQuantity: number;
  safra: Safra;
}
