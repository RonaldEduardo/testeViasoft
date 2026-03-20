package com.ronald.gustmann.api.mapper;

import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.dto.product.ProductRequestDTO;
import com.ronald.gustmann.api.dto.product.ProductResponseDTO;
import com.ronald.gustmann.api.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductCreateDTO dto);

    ProductResponseDTO toResponse(Product entity);

    List<ProductResponseDTO> toResponseList(List<Product> entities);

    void updateFromRequest(ProductRequestDTO dto, @MappingTarget Product entity);
}

