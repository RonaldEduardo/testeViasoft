import { SaleItemRequestDTO } from './sale-item-request.dto';

export interface SaleCreateDTO {
  producerId: number;
  items: SaleItemRequestDTO[];
  totalValue: number;
}
