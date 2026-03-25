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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        Product savedProduct = productService.create(productCreateDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(uri).body(productService.findById(savedProduct.getId()));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAllProduct(ProductCreateDTO productCreateDTO) {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable("id") Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok().body(productService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
