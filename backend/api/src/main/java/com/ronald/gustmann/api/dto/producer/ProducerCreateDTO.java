package com.ronald.gustmann.api.dto.producer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProducerCreateDTO(
        @NotBlank(message = "O nome não pode ser vazio")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        Double creditLimit,

        @NotNull(message = "A categoria é obrigatória")
        @Size(min = 3, max = 14, message = "O CNPJ deve ter entre 3 e 14 caracteres")
        String cnpj
) {
}
