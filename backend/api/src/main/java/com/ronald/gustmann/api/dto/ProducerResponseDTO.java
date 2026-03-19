package com.ronald.gustmann.api.dto;

import com.ronald.gustmann.api.model.Producer;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProducerResponseDTO(
        String name,

        Double creditLimit,

        String cnpj
) {
    public ProducerResponseDTO(Producer entity) {
        this(
                entity.getName(),
                entity.getCreditLimit(),
                entity.getCnpj()
        );
    }
}
