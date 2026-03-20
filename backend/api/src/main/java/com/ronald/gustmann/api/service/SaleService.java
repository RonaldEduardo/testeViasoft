package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.sale.SaleCreateDTO;
import com.ronald.gustmann.api.dto.sale.SaleRequestDTO;
import com.ronald.gustmann.api.dto.sale.SaleResponseDTO;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.Sale;
import com.ronald.gustmann.api.repository.ProducerRepository;
import com.ronald.gustmann.api.repository.ProductRepository;
import com.ronald.gustmann.api.repository.SaleRepository;
import com.ronald.gustmann.api.utils.SeasonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Sale create(SaleCreateDTO saleCreateDTO) {
        Producer producer = producerRepository.findById(saleCreateDTO.producerId())
                .orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));

        List<Product> products = productRepository.findAllById(saleCreateDTO.productIds());
        if (products.size() != saleCreateDTO.productIds().size()) {
            throw new RuntimeException("Produto nao encontrado");
        }

        products = this.applyingDiscount(products);

        Double totalValue = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        if (!validateProducerCredit(producer, totalValue)) {
            throw new RuntimeException("Limite de credito insuficiente");
        }

        Sale sale = new Sale();
        sale.setProducer(producer);
        sale.setProducts(products);
        sale.setTotalValue(totalValue);
        return saleRepository.save(sale);
    }

    @Transactional
    public List<SaleResponseDTO> findAll() {
        return saleRepository.findAll().stream()
                .map(SaleResponseDTO::new)
                .toList();
    }

    @Transactional
    public SaleResponseDTO findById(Long id) {
        return new SaleResponseDTO(saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda nao encontrada")));
    }

    @Transactional
    public SaleResponseDTO update(Long id, SaleRequestDTO saleRequestDTO) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda nao encontrada"));

        Producer producer = producerRepository.findById(saleRequestDTO.producerId()).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Produtor nao encontrado"));

        List<Long> currentIds = sale.getProducts().stream().map(Product::getId).toList();
        List<Long> incomingIds = saleRequestDTO.productIds();

        if (incomingIds.equals(currentIds)) return null;

        List<Product> products = productRepository.findAllById(incomingIds);
        if (products.size() != incomingIds.size()) {
            throw new RuntimeException("Produto nao encontrado");
        }

        Double totalValue = products.stream().mapToDouble(Product::getPrice).sum();
        if (!validateProducerCredit(producer, totalValue)) {
            throw new RuntimeException("Limite de credito insuficiente");
        }

        sale.setProducts(products);
        sale.setTotalValue(totalValue);

        sale = saleRepository.save(sale);
        return new SaleResponseDTO(sale);
    }

    @Transactional
    public void delete(Long id) {
        saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Venda nao encontrada"));
        saleRepository.deleteById(id);
    }

    private boolean validateProducerCredit(Producer producer, Double totalValue) {
        return producer.getCreditLimit() >= totalValue;
    }

    private List<Product> applyingDiscount(List<Product> products) {
        products.forEach(p -> {
            if (p.getSafra().equals(SeasonUtils.getSeason(LocalDate.now()))) {
                p.setPrice(p.getPrice() * 0.05);
            } else {
                p.setPrice(p.getPrice());
            }
        });

        return products;
    }
}

