package com.ronald.gustmann.api.dto.producer;

import com.ronald.gustmann.api.model.Producer;

public record ProducerResponseDTO(
        Long id,

        String name,

        Double creditLimit,

        String cnpj
) {
    public ProducerResponseDTO(Producer entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getCreditLimit(),
                entity.getCnpj()
        );
    }
}
