package com.ronald.gustmann.api.controller;

import com.ronald.gustmann.api.dto.producer.ProducerResponseDTO;
import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.dto.product.ProductRequestDTO;
import com.ronald.gustmann.api.dto.product.ProductResponseDTO;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        productService.create(productCreateDTO);
        return ResponseEntity.created(null).build();
    }

    @GetMapping
    public ResponseEntity findAllProduct(ProductCreateDTO productCreateDTO) {
            return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProducerById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok().body(productService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
