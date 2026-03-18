package com.ronald.gustmann.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(length = 14)
    private String cnpj;

    @Column(length = 20)
    private Double creditLimit;
}
