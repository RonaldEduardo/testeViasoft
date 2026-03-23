export interface SaleItemResultDTO {
  productId: number;
  quantity: number;
  unitPrice: number;
  grossTotal: number;
  discountAmount: number;
  netTotal: number;
  discountReason?: string;
}
