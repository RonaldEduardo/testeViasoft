package com.ronald.gustmann.api.repository;

import com.ronald.gustmann.api.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProducerRepository  extends JpaRepository<Producer, Long> {
    public List<Producer> findAll();
}
