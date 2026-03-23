import { SaleItemResponseDTO } from './sale-item-response.dto';

export interface SaleResponseDTO {
  id?: number;
  saleId?: number;
  producerId: number;
  producerName?: string;
  totalValue?: number;
  grossTotal?: number;
  netTotal?: number;
  createdAt?: string;
  saleDate?: string;
  items: SaleItemResponseDTO[];
}
