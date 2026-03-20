package com.ronald.gustmann.api.mapper;

import com.ronald.gustmann.api.dto.sale.SaleItemResponseDTO;
import com.ronald.gustmann.api.dto.sale.SaleResponseDTO;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Sale;
import com.ronald.gustmann.api.model.SaleItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaleMapper {
    public SaleResponseDTO toResponse(Sale entity) {
        List<SaleItemResponseDTO> items = entity.getSaleItems().stream()
                .map(item -> new SaleItemResponseDTO(
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPriceAtTimeOfSale()
                ))
                .toList();
        return new SaleResponseDTO(entity.getProducer().getId(), items, entity.getTotalValue());
    }

    public Sale toEntity(Producer producer, List<SaleItem> saleItems, Double totalValue) {
        Sale sale = new Sale();
        sale.setProducer(producer);
        sale.setTotalValue(totalValue);
        sale.setSaleItems(saleItems);
        return sale;
    }

    public List<SaleResponseDTO> toResponseList(List<Sale> entities) {
        return entities.stream().map(this::toResponse).toList();
    }
}

