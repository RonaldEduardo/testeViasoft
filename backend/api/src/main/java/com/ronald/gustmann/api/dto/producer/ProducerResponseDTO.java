package com.ronald.gustmann.api.dto.producer;

import com.ronald.gustmann.api.model.Producer;

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
