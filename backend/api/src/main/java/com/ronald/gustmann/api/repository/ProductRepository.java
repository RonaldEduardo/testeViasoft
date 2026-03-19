package com.ronald.gustmann.api.repository;

import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAll();
}
