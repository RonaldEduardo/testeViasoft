package com.ronald.gustmann.api.repository;

import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
