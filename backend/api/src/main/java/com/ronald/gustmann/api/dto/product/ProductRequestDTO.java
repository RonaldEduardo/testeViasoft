package com.ronald.gustmann.api.dto.product;

import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.enums.Safra;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @Positive(message = "O preço deve ser maior que zero")
        Double price,

        Category category,

        Safra safra
) {

    public ProductRequestDTO(Product entity) {
        this(
                entity.getName(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getSafra()
        );
    }
}
