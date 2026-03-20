package com.ronald.gustmann.api.dto.sale;

public record SaleItemResponseDTO(
        Long productId,
        Integer quantity,
        Double priceAtTimeOfSale
) {
}

