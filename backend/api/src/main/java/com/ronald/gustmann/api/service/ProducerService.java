package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.*;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.repository.ProducerRepository;
import com.ronald.gustmann.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {
    @Autowired
    private ProducerRepository producerRepository;

    @Transactional
    public Producer create(ProducerCreateDTO producerCreateDTO) {
        Producer producer = new Producer();
        producer.setName(producerCreateDTO.name());
        producer.setCnpj(producerCreateDTO.cnpj());
        producer.setCreditLimit(producerCreateDTO.creditLimit());
        return producerRepository.save(producer);
    }

    @Transactional
    public List<ProducerResponseDTO> findAll() {
        return producerRepository.findAll().stream()
                .map(ProducerResponseDTO::new)
                .toList();
    }

    @Transactional
    public ProducerResponseDTO findById(Long id) {
        return new ProducerResponseDTO(producerRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Produtor nao encontrado")));
    }

    @Transactional
    public ProducerResponseDTO update(Long id, ProducerRequestDTO producerRequestDTO) {
        Producer producer = producerRepository.findById(id).orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));
        producer.setName(producerRequestDTO.name());
        producer.setCnpj(producerRequestDTO.cnpj());
        producer.setCreditLimit(producerRequestDTO.creditLimit());

        producer = producerRepository.save(producer);
        return new ProducerResponseDTO(producer);
    }

    @Transactional
    public void delete(Long id) {
        Producer producer = producerRepository.findById(id).orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));
        producerRepository.deleteById(id);
    }
}
