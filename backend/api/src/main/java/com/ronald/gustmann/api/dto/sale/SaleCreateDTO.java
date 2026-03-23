package com.ronald.gustmann.api.dto.sale;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SaleCreateDTO(
        @NotNull(message = "O produtor e obrigatorio")
        Long producerId,

        @NotEmpty(message = "A lista de itens e obrigatoria")
        List<SaleItemDTO> items,

        @NotNull(message = "O total e obrigatoria")
        @Positive(message = "O total deve ser maior que zero")
        Double totalValue
) {
}

