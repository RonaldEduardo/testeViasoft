import { SaleItemResultDTO } from './sale-item-result.dto';

export interface SaleCreateResponseDTO {
  saleId?: number;
  grossTotal: number;
  discountTotal: number;
  netTotal: number;
  items: SaleItemResultDTO[];
}
