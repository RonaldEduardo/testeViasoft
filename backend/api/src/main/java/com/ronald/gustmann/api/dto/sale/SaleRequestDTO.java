package com.ronald.gustmann.api.dto.sale;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SaleRequestDTO(
        @NotNull(message = "O produtor e obrigatorio")
        Long producerId,

        @NotEmpty(message = "A lista de produtos e obrigatoria")
        List<Long> productIds,

        @NotNull(message = "O valor total e obrigatorio")
        @Positive(message = "O valor total deve ser maior que zero")
        Double totalValue
) {
}

