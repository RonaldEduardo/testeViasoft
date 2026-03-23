package com.ronald.gustmann.api.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleItemDTO(
        @NotNull(message = "O produto e obrigatorio")
        Long productId,

        @NotNull(message = "A quantidade e obrigatoria")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity,

        @NotNull(message = "O total e obrigatoria")
        @Positive(message = "O total deve ser maior que zero")
        Double total
) {
}

