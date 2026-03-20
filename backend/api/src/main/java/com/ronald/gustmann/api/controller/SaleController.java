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
        try {
            saleService.create(saleCreateDTO);
            return ResponseEntity.created(null).build();
        } catch (ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(409).body("Conflito de concorrencia no estoque. Tente novamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findSaleById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(saleService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SaleResponseDTO>> findAllSales() {
        try {
            return ResponseEntity.ok(saleService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<SaleResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SaleRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(saleService.update(id, dto));
        } catch (ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

