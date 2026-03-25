package com.ronald.gustmann.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_producer")
    private Producer producer;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> saleItems = new ArrayList<>();

    @Column
    private Double totalValue;

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems.clear();
        if (saleItems == null) {
            return;
        }
        saleItems.forEach(item -> item.setSale(this));
        this.saleItems.addAll(saleItems);
    }
}
