package com.ronald.gustmann.api.mapper;

import com.ronald.gustmann.api.dto.sale.SaleCreateDTO;
import com.ronald.gustmann.api.dto.sale.SaleResponseDTO;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    @Mapping(target = "producerId", source = "producer.id")
    @Mapping(target = "productIds", source = "products")
    SaleResponseDTO toResponse(Sale entity);

    @Mapping(target = "id", ignore = true) // O ID é gerado pelo banco
    @Mapping(target = "producer", source = "producer")
    @Mapping(target = "products", source = "products")
    @Mapping(target = "totalValue", source = "totalValue")
    Sale toEntity(Producer producer, List<Product> products, Double totalValue);

    List<SaleResponseDTO> toResponseList(List<Sale> entities);

    default List<Long> mapProductsToIds(List<Product> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream().map(Product::getId).toList();
    }
}

