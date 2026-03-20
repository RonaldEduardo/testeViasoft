package com.ronald.gustmann.api.mapper;

import com.ronald.gustmann.api.dto.producer.ProducerCreateDTO;
import com.ronald.gustmann.api.dto.producer.ProducerRequestDTO;
import com.ronald.gustmann.api.dto.producer.ProducerResponseDTO;
import com.ronald.gustmann.api.model.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProducerMapper {
    Producer toEntity(ProducerCreateDTO dto);

    ProducerResponseDTO toResponse(Producer entity);

    List<ProducerResponseDTO> toResponseList(List<Producer> entities);

    void updateFromRequest(ProducerRequestDTO dto, @MappingTarget Producer entity);
}

