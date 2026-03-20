package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.producer.ProducerCreateDTO;
import com.ronald.gustmann.api.dto.producer.ProducerRequestDTO;
import com.ronald.gustmann.api.dto.producer.ProducerResponseDTO;
import com.ronald.gustmann.api.mapper.ProducerMapper;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.repository.ProducerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {
    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProducerMapper producerMapper;

    @Transactional
    public Producer create(ProducerCreateDTO producerCreateDTO) {
        Producer producer = producerMapper.toEntity(producerCreateDTO);
        return producerRepository.save(producer);
    }

    @Transactional
    public List<ProducerResponseDTO> findAll() {
        return producerMapper.toResponseList(producerRepository.findAll());
    }

    @Transactional
    public ProducerResponseDTO findById(Long id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));
        return producerMapper.toResponse(producer);
    }

    @Transactional
    public ProducerResponseDTO update(Long id, ProducerRequestDTO producerRequestDTO) {
        Producer producer = producerRepository.findById(id).orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));
        producerMapper.updateFromRequest(producerRequestDTO, producer);

        producer = producerRepository.save(producer);
        return producerMapper.toResponse(producer);
    }

    @Transactional
    public void delete(Long id) {
        Producer producer = producerRepository.findById(id).orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));
        producerRepository.deleteById(id);
    }
}
