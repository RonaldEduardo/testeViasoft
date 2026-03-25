package com.ronald.gustmann.api.controller;

import com.ronald.gustmann.api.dto.sale.*;
import com.ronald.gustmann.api.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/create")
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleCreateDTO saleCreateDTO) {
        SaleResponseDTO response = saleService.create(saleCreateDTO);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SaleResponseDTO>> findAllSales() {
        return ResponseEntity.ok(saleService.findAll());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SaleResponseDTO> update(@PathVariable("id") Long id, @Valid @RequestBody SaleRequestDTO dto) {
        return ResponseEntity.ok().body(saleService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculate-item")
    public ResponseEntity<SaleItemResponseDTO> calculateItem(@RequestBody SaleItemDTO itemDTO) {
        SaleItemResponseDTO calculated = saleService.calculateItemPrice(itemDTO);
        return ResponseEntity.ok(calculated);
    }
}
