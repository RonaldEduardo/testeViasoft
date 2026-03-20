package com.ronald.gustmann.api.controller;

import com.ronald.gustmann.api.dto.producer.ProducerCreateDTO;
import com.ronald.gustmann.api.dto.producer.ProducerRequestDTO;
import com.ronald.gustmann.api.dto.producer.ProducerResponseDTO;
import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.service.ProducerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producers")
public class ProducerController {
    @Autowired
    private ProducerService producerService;

    @PostMapping("/create")
    public ResponseEntity<ProducerResponseDTO> create(@Valid @RequestBody ProducerCreateDTO producerCreateDTO) {
        producerService.create(producerCreateDTO);
        return ResponseEntity.created(null).build(); // TODO: analise sobre o location
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerResponseDTO> findProducerById(@PathVariable Long id) {
        return ResponseEntity.ok(producerService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProducerResponseDTO>> findAllProducers(ProductCreateDTO productCreateDTO) {
        return ResponseEntity.ok(producerService.findAll());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProducerResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProducerRequestDTO dto) {
        return ResponseEntity.ok().body(producerService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        producerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
