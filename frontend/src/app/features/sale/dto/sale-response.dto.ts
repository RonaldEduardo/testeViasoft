import { SaleItemResponseDTO } from './sale-item-response.dto';

export interface SaleResponseDTO {
  id?: number;
  producerId: number;
  totalValue?: number;
  items: SaleItemResponseDTO[];
}
