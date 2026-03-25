package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.sale.SaleItemDTO;
import com.ronald.gustmann.api.dto.sale.SaleItemResponseDTO;
import com.ronald.gustmann.api.model.Product;
import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.model.enums.Safra;
import com.ronald.gustmann.api.repository.ProductRepository;
import com.ronald.gustmann.api.utils.SeasonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SaleServiceDiscountTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductRepository productRepository;

    private Product productDaSafraAtual;
    private Product productDeSafraDistinta;

    @BeforeEach
    void setUp() {
        String safraAtualNome = SeasonUtils.getSeason(LocalDate.now());
        Safra safraAtual = Safra.valueOf(safraAtualNome);

        productDaSafraAtual = new Product();
        productDaSafraAtual.setName("Produto da Safra Atual");
        productDaSafraAtual.setPrice(100.00);
        productDaSafraAtual.setCategory(Category.SEMENTE);
        productDaSafraAtual.setStockQuantity(50);
        productDaSafraAtual.setSafra(safraAtual);
        productDaSafraAtual = productRepository.save(productDaSafraAtual);

        Safra safraDistinta = safraAtual == Safra.VERAO ? Safra.INVERNO : Safra.VERAO;

        productDeSafraDistinta = new Product();
        productDeSafraDistinta.setName("Produto de Safra Distinta");
        productDeSafraDistinta.setPrice(200.00);
        productDeSafraDistinta.setCategory(Category.FERTILIZANTE);
        productDeSafraDistinta.setStockQuantity(50);
        productDeSafraDistinta.setSafra(safraDistinta);
        productDeSafraDistinta = productRepository.save(productDeSafraDistinta);
    }

    @Test
    @DisplayName("Deve aplicar 5% de desconto quando safra do produto corresponde à estação atual")
    void deveAplicarDescontoNaSafraCorrespondente() {
        SaleItemDTO dto = new SaleItemDTO(productDaSafraAtual.getId(), 1, 100.00);

        SaleItemResponseDTO result = saleService.calculateItemPrice(dto);

        double precoEsperado = 100.00 * 0.95; // 5% de desconto
        assertThat(result.discountValue()).isEqualTo(5.00, within(0.001));
        assertThat(result.priceAtTimeOfSale()).isEqualTo(precoEsperado, within(0.001));
        assertThat(result.originalUnitPrice()).isEqualTo(100.00, within(0.001));
    }

    @Test
    @DisplayName("Não deve aplicar desconto quando safra do produto não corresponde à estação atual")
    void naoDeveAplicarDescontoEmSafraDistinta() {
        SaleItemDTO dto = new SaleItemDTO(productDeSafraDistinta.getId(), 1, 200.00);

        SaleItemResponseDTO result = saleService.calculateItemPrice(dto);

        assertThat(result.discountValue()).isEqualTo(0.0, within(0.001));
        assertThat(result.priceAtTimeOfSale()).isEqualTo(200.00, within(0.001));
        assertThat(result.originalUnitPrice()).isEqualTo(200.00, within(0.001));
    }

    @Test
    @DisplayName("Deve retornar preço original e não aplicar desconto duplo independente da quantidade")
    void deveManterPrecoUnitarioIndependenteDaQuantidade() {
        SaleItemDTO dto = new SaleItemDTO(productDaSafraAtual.getId(), 3, 300.00);

        SaleItemResponseDTO result = saleService.calculateItemPrice(dto);

        assertThat(result.discountValue()).isEqualTo(5.00, within(0.001));
        assertThat(result.quantity()).isEqualTo(3);
    }
}
