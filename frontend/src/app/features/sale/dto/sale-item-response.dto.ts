export interface SaleItemResponseDTO {
  productId: number;
  quantity: number;
  productName?: string;
  priceAtTimeOfSale?: number;
  unitPrice?: number;
  total?: number;
  grossTotal?: number;
  netTotal?: number;
}
