package com.ronald.gustmann.api.dto.product;

import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.model.enums.Safra;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductCreateDTO (
        @NotBlank(message = "O nome não pode ser vazio")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        Double price,

        @NotNull(message = "A categoria é obrigatória")
        Category category,

        String recipeProduct,

        @NotNull(message = "A quantidade em estoque e obrigatoria")
        @PositiveOrZero(message = "A quantidade em estoque nao pode ser negativa")
        Integer stockQuantity,

        @NotNull(message = "A safra é obrigatória")
        Safra safra
){}
