import { SaleItemDTO } from '../dto/sale-item.dto';
import { Producer } from './producer';

export interface Sale {
  id: number;
  producer: Producer;
  saleItems: SaleItemDTO[];
  totalValue: number;
}
