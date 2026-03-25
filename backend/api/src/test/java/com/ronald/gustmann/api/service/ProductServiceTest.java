package com.ronald.gustmann.api.service;

import com.ronald.gustmann.api.dto.product.ProductCreateDTO;
import com.ronald.gustmann.api.dto.product.ProductResponseDTO;
import com.ronald.gustmann.api.exceptions.DefensiveNotContainRecipeException;
import com.ronald.gustmann.api.exceptions.EntityNotFoundException;
import com.ronald.gustmann.api.model.enums.Category;
import com.ronald.gustmann.api.model.enums.Safra;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("Deve criar produto do tipo FERTILIZANTE com sucesso")
    void deveCriarProdutoFertilizante() {
        var dto = new ProductCreateDTO(
                "Ureia Granulada",
                150.00,
                Category.FERTILIZANTE,
                null,
                100,
                Safra.VERAO);

        var saved = productService.create(dto);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar DEFENSIVO sem receita")
    void deveLancarExcecaoDefensivoSemReceita() {
        var dto = new ProductCreateDTO(
                "Herbicida XYZ",
                200.00,
                Category.DEFENSIVO,
                null, // receita ausente
                50,
                Safra.INVERNO);

        assertThatThrownBy(() -> productService.create(dto))
                .isInstanceOf(DefensiveNotContainRecipeException.class)
                .hasMessageContaining("receita");
    }

    @Test
    @DisplayName("Deve criar DEFENSIVO quando receita é informada")
    void deveCriarDefensivoComReceita() {
        var dto = new ProductCreateDTO(
                "Glifosato",
                350.00,
                Category.DEFENSIVO,
                "Receita Agronômica #001",
                30,
                Safra.OUTONO);

        var saved = productService.create(dto);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao buscar produto inexistente")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        assertThatThrownBy(() -> productService.findById(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Deve retornar o produto correto ao buscar por ID")
    void deveRetornarProdutoPorId() {
        var dto = new ProductCreateDTO(
                "Semente de Soja",
                80.00,
                Category.SEMENTE,
                null,
                200,
                Safra.PRIMAVERA);

        var saved = productService.create(dto);
        ProductResponseDTO found = productService.findById(saved.getId());

        assertThat(found.name()).isEqualTo("Semente de Soja");
        assertThat(found.price()).isEqualTo(80.00);
        assertThat(found.category()).isEqualTo(Category.SEMENTE);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao deletar produto inexistente")
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        assertThatThrownBy(() -> productService.delete(9999L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
