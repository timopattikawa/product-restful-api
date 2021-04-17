package com.kamoro.product.api.service;

import com.kamoro.product.api.Exception.ApiBadRequestException;
import com.kamoro.product.api.Exception.ApiNotFoundException;
import com.kamoro.product.api.entity.Product;
import com.kamoro.product.api.model.ProductRequest;
import com.kamoro.product.api.model.ProductResponse;
import com.kamoro.product.api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    private ProductService underTestService;

    @BeforeEach
    void setUp() {
        underTestService = new ProductService(productRepository);
    }

    @Test
    void ItShouldSuccess_createNewProduct() {
        ProductRequest productRequest = new ProductRequest(
                "Susu Bearbrand",
                "5000",
                10
        );
        Product product = new Product(
                null,
                "Susu Bearbrand",
                "5000",
                10
        );

        underTestService.createNewProduct(productRequest);
        ProductResponse productResponse = underTestService.createNewProduct(productRequest);

        assertThat(productResponse.getProductName()).isEqualTo(product.getProductName());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
        assertThat(productResponse.getQuantity()).isEqualTo(product.getQuantity());
    }

    @Test
    void ItShouldFail_createNewProduct_CauseFoundSame() {
        ProductRequest productReq = new ProductRequest(
                "Susu Bearbrand",
                "5000",
                10
        );

        Product product = new Product(
                null,
                "Susu Bearbrand",
                "5000",
                10
        );

        when(productRepository.findByName(productReq.getProductName())).thenReturn(java.util.Optional.of(product));
        assertThatThrownBy(() -> underTestService.createNewProduct(productReq))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Product with name " + product.getProductName() + " has been added");
        verify(productRepository, never()).save(product);
    }

    @Test
    void ItShouldFail_createNewProduct_CauseEmptyName() {
        ProductRequest productReq = new ProductRequest(
                null,
                "5000",
                10
        );

        assertThatThrownBy(() -> underTestService.createNewProduct(productReq))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Product name cannot empty");
        verify(productRepository, never()).save(productReq.productRequestToProduct());
    }

    @Test
    void ItShouldFail_createNewProduct_CauseEmptyPrice() {
        ProductRequest product = new ProductRequest(
                "Susu Bearbrand",
                null,
                10
        );

        assertThatThrownBy(() -> underTestService.createNewProduct(product))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Product price cannot empty");
        verify(productRepository, never()).save(product.productRequestToProduct());
    }

    @Test
    void ItShouldFail_createNewProduct_CauseNullQuantity() {
        ProductRequest product = new ProductRequest(
                "Susu Bearbrand",
                "5000",
                null
        );

        assertThatThrownBy(() -> underTestService.createNewProduct(product))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Product quantity cannot empty");
        verify(productRepository, never()).save(product.productRequestToProduct());
    }

    @Test
    void ItShouldSuccess_getAllProduct() {
        underTestService.getAllProduct();
        verify(productRepository).findAll();
    }

    @Test
    void ItShouldSuccess_getProduct() {
        Product product = new Product(
                1L,
                "Susu Bearbrand",
                "5000",
                null
        );
        when(productRepository.findById(product.getProductId())).thenReturn(java.util.Optional.of(product));
        ProductResponse underTestServiceProduct = underTestService.getProduct(product.getProductId());

        verify(productRepository).findById(1L);
        assertThat(underTestServiceProduct.getProductName()).isEqualTo(product.getProductName());
        assertThat(underTestServiceProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(underTestServiceProduct.getQuantity()).isEqualTo(product.getQuantity());

    }

    @Test
    void ItShouldFail_getProduct_CauseNotFound() {
        Product product = new Product(
                1L,
                "Susu Bearbrand",
                "5000",
                null
        );
        assertThatThrownBy(() -> underTestService.getProduct(1L))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Not Found product with id: " + 1L);
        verify(productRepository).findById(1L);
    }
}