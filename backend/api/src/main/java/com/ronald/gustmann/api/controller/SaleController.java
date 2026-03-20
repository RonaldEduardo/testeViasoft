package com.ronald.gustmann.api.controller;

import com.ronald.gustmann.api.dto.sale.SaleCreateDTO;
import com.ronald.gustmann.api.dto.sale.SaleRequestDTO;
import com.ronald.gustmann.api.dto.sale.SaleResponseDTO;
import com.ronald.gustmann.api.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody SaleCreateDTO saleCreateDTO) {
        saleService.create(saleCreateDTO);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SaleResponseDTO>> findAllSales() {
        return ResponseEntity.ok(saleService.findAll());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SaleResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SaleRequestDTO dto) {
        return ResponseEntity.ok().body(saleService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

