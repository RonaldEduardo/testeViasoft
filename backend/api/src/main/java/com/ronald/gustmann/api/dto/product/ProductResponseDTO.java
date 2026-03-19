package com.ronald.gustmann.api.dto.product;

import com.ronald.gustmann.api.model.Category;
import com.ronald.gustmann.api.model.Product;

public record ProductResponseDTO(
        String name,

        Double price,

        Category category
) {

    public ProductResponseDTO(Product entity) {
        this(
                entity.getName(),
                entity.getPrice(),
                entity.getCategory()
        );
    }
}