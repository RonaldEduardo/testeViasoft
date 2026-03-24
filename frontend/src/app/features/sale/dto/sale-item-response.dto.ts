export interface SaleItemResponseDTO {
  productId: number;
  quantity: number;
  productName?: string;
  priceAtTimeOfSale?: number;
  originalUnitPrice?: number;
  discountValue?: number;
  total?: number;
  grossTotal?: number;
  netTotal?: number;
}
