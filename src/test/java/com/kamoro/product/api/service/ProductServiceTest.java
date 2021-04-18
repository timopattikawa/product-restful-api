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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
                1L,
                "Susu Bearbrand",
                "5000",
                10
        );

        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductResponse productResponse = underTestService.createNewProduct(productRequest);

        assertThat(productResponse.getProductName()).isEqualTo(product.getProductName());
        assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
        assertThat(productResponse.getQuantity()).isEqualTo(product.getQuantity());
        verify(productRepository).save(any(Product.class));
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
        verify(productRepository, never()).save(any(Product.class));
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
        verify(productRepository, never()).save(any(Product.class));
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
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void ItShouldSuccess_getAllProduct() {
        underTestService.getAllProduct();
        verify(productRepository).findAll();
    }

    @Test
    void ItShouldSuccess_getAllProduct_whenCopyToProductResponse() {
        List<Product> products = List.of(
                new Product(1L, "Susu Bearbrand", "5000", 1),
                new Product(2L, "Susu Ultra Milk", "5000", 1)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> productResponses = underTestService.getAllProduct();

        assertThat(productResponses.size()).isEqualTo(products.size());
        for (int i = 0; i<productResponses.size(); i++) {
            assertThat(productResponses.get(i).getProductId()).isEqualTo(products.get(i).getProductId());
            assertThat(productResponses.get(i).getProductName()).isEqualTo(products.get(i).getProductName());
            assertThat(productResponses.get(i).getPrice()).isEqualTo(products.get(i).getPrice());
            assertThat(productResponses.get(i).getQuantity()).isEqualTo(products.get(i).getQuantity());
        }
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

    @Test
    public void ItShouldSuccess_updateProduct_All() {
        Product product = new Product(
                1L,
                "Susu Bearbrand",
                "5000",
                10
        );

        ProductRequest productRequest =  new ProductRequest(
                "Susu BearBrand",
                "10000",
                20
        );

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        underTestService.updateProduct(1L, productRequest);

        verify(productRepository).findById(1L);
        assertThat(product.getProductId()).isEqualTo(1L);
        assertThat(product.getProductName()).isEqualTo(productRequest.getProductName());
        assertThat(product.getPrice()).isEqualTo(productRequest.getPrice());
        assertThat(product.getQuantity()).isEqualTo(productRequest.getQuantity());
    }

    @Test
    public void ItShouldFail_updateProduct_CauseNotFoundProduct() {

        ProductRequest productRequest =  new ProductRequest(
                "Susu BearBrand",
                "10000",
                20
        );

        Long productID = 1L;

        assertThatThrownBy(() -> underTestService.updateProduct(productID, productRequest))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Not Found product with id: " + productID);
        verify(productRepository).findById(productID);
    }

    @Test
    public void itShouldSuccess_deleteProduct() {
        Long productID = 1L;
        Product product = new Product(
                1L,
                "Susu Bearbrand",
                "5000",
                10
        );
        when(productRepository.findById(productID)).thenReturn(java.util.Optional.of(product));
        underTestService.deleteProduct(productID);

        verify(productRepository).findById(productID);
        verify(productRepository).delete(product);
    }

    @Test
    public void itShouldFail_deleteProduct_CauseNotFound() {
        Long productID = 1L;
        Product product = new Product(
                1L,
                "Susu Bearbrand",
                "5000",
                10
        );
        assertThatThrownBy(() -> underTestService.deleteProduct(productID))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Not found product with id: " + productID);
        verify(productRepository).findById(productID);
        verify(productRepository, never()).delete(product);
    }
}