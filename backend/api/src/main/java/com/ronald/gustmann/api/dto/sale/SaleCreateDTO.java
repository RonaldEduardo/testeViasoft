package com.ronald.gustmann.api.dto.sale;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleCreateDTO(
        @NotNull(message = "O produtor e obrigatorio")
        Long producerId,

        @NotEmpty(message = "A lista de produtos e obrigatoria")
        List<Long> productIds
) {
}

