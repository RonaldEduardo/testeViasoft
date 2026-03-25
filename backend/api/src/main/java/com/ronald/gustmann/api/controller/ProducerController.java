package com.ronald.gustmann.api.controller;

import com.ronald.gustmann.api.dto.producer.ProducerCreateDTO;
import com.ronald.gustmann.api.dto.producer.ProducerRequestDTO;
import com.ronald.gustmann.api.dto.producer.ProducerResponseDTO;
import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.service.ProducerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/producers")
public class ProducerController {
    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProducerResponseDTO> create(@Valid @RequestBody ProducerCreateDTO producerCreateDTO) {
        Producer savedProducer = producerService.create(producerCreateDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProducer.getId())
                .toUri();
        return ResponseEntity.created(
                uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerResponseDTO> findProducerById(@PathVariable Long id) {
        return ResponseEntity.ok(producerService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProducerResponseDTO>> findAllProducers() {
        return ResponseEntity.ok(producerService.findAll());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProducerResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody ProducerRequestDTO dto) {
        return ResponseEntity.ok().body(producerService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        producerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
