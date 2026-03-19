package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.ProductCreateDTO;
import com.ronald.gustmann.api.dto.ProductRequestDTO;
import com.ronald.gustmann.api.dto.ProductResponseDTO;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product create(ProductCreateDTO productCreateDTO) {
        Product product = new Product();
        product.setName(productCreateDTO.name());
        product.setCategory(productCreateDTO.category());
        product.setPrice(productCreateDTO.price());
        return productRepository.save(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product nao encontrado"));
        product.setName(productRequestDTO.name());
        product.setCategory(productRequestDTO.category());
        product.setPrice(productRequestDTO.price());

        product =  productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Transactional
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product nao encontrado"));
        productRepository.deleteById(id);
    }
}
