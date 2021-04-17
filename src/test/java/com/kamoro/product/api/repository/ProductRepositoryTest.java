package com.kamoro.product.api.repository;

import com.kamoro.product.api.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void ItShouldSuccess_findByName() {
        Product product = new Product(
                1L,
                "Susu Bearbrand",
                "5000",
                10
        );
        productRepository.save(product);

        Optional<Product> productRepositoryByName = productRepository.findByName(product.getProductName());
        assertThat(productRepositoryByName.isPresent()).isTrue();
    }
}