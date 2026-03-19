package com.ronald.gustmann.api.dto;

import com.ronald.gustmann.api.model.Category;
import com.ronald.gustmann.api.model.Product;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

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