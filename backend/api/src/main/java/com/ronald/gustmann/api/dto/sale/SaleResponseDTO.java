package com.ronald.gustmann.api.dto.sale;

import java.util.List;

public record SaleResponseDTO(
        Long producerId,
        List<SaleItemResponseDTO> items,
        Double totalValue
) {
}

