package com.ronald.gustmann.api.dto.product;

import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.enums.Safra;

public record ProductResponseDTO(
        String name,

        Double price,

        Category category,

        Integer stockQuantity,

        Safra safra
) {

    public ProductResponseDTO(Product entity) {
        this(
                entity.getName(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getStockQuantity(),
                entity.getSafra()
        );
    }
}