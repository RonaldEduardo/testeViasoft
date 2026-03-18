package com.ronald.gustmann.api.repository;

import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
