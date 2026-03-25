package com.ronald.gustmann.api.model;

import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.model.enums.Safra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Category category;

    @Column(nullable = false)
    private Double price;

    @Column(length = 100)
    private String recipeProduct;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Safra safra;
}
