package com.ronald.gustmann.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "seles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_producer")
    private Producer producer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "sale_products",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Column(length = 20)
    private Double totalValue;
}
