package com.ronald.gustmann.api.dto.sale;

import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.Sale;

import java.util.List;

public record SaleResponseDTO(
        Long producerId,
        List<Long> productIds,
        Double totalValue
) {
    public SaleResponseDTO(Sale entity) {
        this(
                entity.getProducer().getId(),
                entity.getProducts().stream().map(Product::getId).toList(),
                entity.getTotalValue()
        );
    }
}

