package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.dto.product.ProductRequestDTO;
import com.ronald.gustmann.api.dto.product.ProductResponseDTO;
import com.ronald.gustmann.api.mapper.ProductMapper;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public Product create(ProductCreateDTO productCreateDTO) {
        if(productCreateDTO.category().equals("DEFENSIVO") && Objects.isNull(productCreateDTO.recipeProduct())) {
            throw new RuntimeException("O defensivo precisa contei uma receita");
        }
        Product product = productMapper.toEntity(productCreateDTO);
        return productRepository.save(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
        productMapper.updateFromRequest(productRequestDTO, product);

        product =  productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional
    public List<ProductResponseDTO> findAll() {
        return productMapper.toResponseList(productRepository.findAll());
    }

    @Transactional
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
        return productMapper.toResponse(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
        productRepository.deleteById(id);
    }
}
