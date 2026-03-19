package com.ronald.gustmann.api.dto;

import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProducerRequestDTO(
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String name,

        @Positive(message = "O preço deve ser maior que zero")
        Double creditLimit,

        @Size(min = 3, max = 14, message = "O CNPJ deve ter entre 3 e 14 caracteres")
        String cnpj
) {
    public ProducerRequestDTO(Producer entity) {
        this(
                entity.getName(),
                entity.getCreditLimit(),
                entity.getCnpj()
        );
    }
}
