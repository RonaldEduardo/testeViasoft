package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.sale.SaleCreateDTO;
import com.ronald.gustmann.api.dto.sale.SaleItemDTO;
import com.ronald.gustmann.api.dto.sale.SaleRequestDTO;
import com.ronald.gustmann.api.dto.sale.SaleResponseDTO;
import com.ronald.gustmann.api.exceptions.EntityNotFoundException;
import com.ronald.gustmann.api.exceptions.InsufficientCreditLimitException;
import com.ronald.gustmann.api.exceptions.InsufficientProductStockException;
import com.ronald.gustmann.api.mapper.SaleMapper;
import com.ronald.gustmann.api.model.Producer;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.Sale;
import com.ronald.gustmann.api.model.SaleItem;
import com.ronald.gustmann.api.repository.ProducerRepository;
import com.ronald.gustmann.api.repository.ProductRepository;
import com.ronald.gustmann.api.repository.SaleRepository;
import com.ronald.gustmann.api.utils.SeasonUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    private final SaleMapper saleMapper;

    private final ProducerRepository producerRepository;

    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository,
                       SaleMapper saleMapper,
                       ProducerRepository producerRepository,
                       ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.producerRepository = producerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Sale create(SaleCreateDTO saleCreateDTO) {
        Producer producer = producerRepository.findById(saleCreateDTO.producerId())
                .orElseThrow(() -> new EntityNotFoundException(Producer.class, saleCreateDTO.producerId()));

        List<SaleItemDTO> requestedItems = saleCreateDTO.items();
        List<Long> productIds = requestedItems.stream().map(SaleItemDTO::productId).distinct().toList();

        Map<Long, Product> productsById = loadProductsById(productIds);
        List<SaleItem> saleItems = buildValidatedSaleItems(requestedItems, productsById);

        Double totalValue = saleItems.stream()
                .mapToDouble(item -> item.getPriceAtTimeOfSale() * item.getQuantity())
                .sum();

        if (!validateProducerCredit(producer, totalValue)) {
            throw new InsufficientCreditLimitException();
        }

        Sale sale = saleMapper.toEntity(producer, saleItems, totalValue);
        return saleRepository.save(sale);
    }

    @Transactional
    public List<SaleResponseDTO> findAll() {
        return saleMapper.toResponseList(saleRepository.findAll());
    }

    @Transactional
    public SaleResponseDTO findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Sale.class, id));
        return saleMapper.toResponse(sale);
    }

    @Transactional
    public SaleResponseDTO update(Long id, SaleRequestDTO saleRequestDTO) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Sale.class, id));

        Producer producer = producerRepository.findById(saleRequestDTO.producerId())
                .orElseThrow(() -> new EntityNotFoundException(Producer.class, saleRequestDTO.producerId()));

        rollbackStockFromSaleItems(sale.getSaleItems());

        List<SaleItemDTO> requestedItems = saleRequestDTO.items();
        List<Long> incomingIds = requestedItems.stream().map(SaleItemDTO::productId).distinct().toList();
        Map<Long, Product> productsById = loadProductsById(incomingIds);
        List<SaleItem> saleItems = buildValidatedSaleItems(requestedItems, productsById);

        Double totalValue = saleItems.stream()
                .mapToDouble(item -> item.getPriceAtTimeOfSale() * item.getQuantity())
                .sum();
        if (!validateProducerCredit(producer, totalValue)) {
            throw new InsufficientCreditLimitException();
        }

        sale.setProducer(producer);
        sale.setSaleItems(saleItems);
        sale.setTotalValue(totalValue);

        sale = saleRepository.save(sale);
        return saleMapper.toResponse(sale);
    }

    @Transactional
    public void delete(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Sale.class, id));
        rollbackStockFromSaleItems(sale.getSaleItems());
        saleRepository.deleteById(id);
    }

    private boolean validateProducerCredit(Producer producer, Double totalValue) {
        return producer.getCreditLimit() >= totalValue;
    }

    private Map<Long, Product> loadProductsById(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new EntityNotFoundException(Product.class);
        }
        Map<Long, Product> productsById = new HashMap<>();
        products.forEach(product -> productsById.put(product.getId(), product));
        return productsById;
    }

    private List<SaleItem> buildValidatedSaleItems(List<SaleItemDTO> requestedItems, Map<Long, Product> productsById) {
        return requestedItems.stream().map(itemDTO -> {
            Product product = productsById.get(itemDTO.productId());
            if (product == null) {
                throw new EntityNotFoundException(Product.class, product.getId());
            }
            if (product.getStockQuantity() < itemDTO.quantity()) {
                throw new InsufficientProductStockException(product);
            }

            product.setStockQuantity(product.getStockQuantity() - itemDTO.quantity());

            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setQuantity(itemDTO.quantity());
            saleItem.setPriceAtTimeOfSale(calculatePriceAtTimeOfSale(product));
            return saleItem;
        }).toList();
    }

    private void rollbackStockFromSaleItems(List<SaleItem> saleItems) {
        saleItems.forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        });
    }

    private Double calculatePriceAtTimeOfSale(Product product) {
        if (product.getSafra().name().equals(SeasonUtils.getSeason(LocalDate.now()))) {
            return product.getPrice() * 0.95;
        }
        return product.getPrice();
    }
}

