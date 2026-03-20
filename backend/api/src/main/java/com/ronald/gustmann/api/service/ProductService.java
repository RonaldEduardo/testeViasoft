package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.dto.product.ProductRequestDTO;
import com.ronald.gustmann.api.dto.product.ProductResponseDTO;
import com.ronald.gustmann.api.exceptions.DefensiveNotContainRecipeException;
import com.ronald.gustmann.api.exceptions.EntityNotFoundException;
import com.ronald.gustmann.api.mapper.ProductMapper;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public Product create(ProductCreateDTO productCreateDTO) {
        if (Category.DEFENSIVO.equals(productCreateDTO.category()) && Objects.isNull(productCreateDTO.recipeProduct())) {
            throw new DefensiveNotContainRecipeException();
        }
        Product product = productMapper.toEntity(productCreateDTO);
        return productRepository.save(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Product.class, id));
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
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        productRepository.deleteById(id);
    }
}
