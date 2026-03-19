package com.ronald.gustmann.api.dto.product;

import com.ronald.gustmann.api.model.Category;
import com.ronald.gustmann.api.model.Product;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @Positive(message = "O preço deve ser maior que zero")
        Double price,

        Category category
) {

    public ProductRequestDTO(Product entity) {
        this(
                entity.getName(),
                entity.getPrice(),
                entity.getCategory()
        );
    }
}
