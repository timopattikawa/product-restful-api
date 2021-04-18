package com.kamoro.product.api.service;

import com.kamoro.product.api.Exception.ApiBadRequestException;
import com.kamoro.product.api.Exception.ApiNotFoundException;
import com.kamoro.product.api.entity.Product;
import com.kamoro.product.api.model.ProductRequest;
import com.kamoro.product.api.model.ProductResponse;
import com.kamoro.product.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createNewProduct(ProductRequest product) {
        Optional<Product> productCheck = productRepository.findByName(product.getProductName());

        if(productCheck.isPresent()) {
             throw new ApiBadRequestException("Product with name " + product.getProductName() + " has been added");
        }

        if(product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new ApiBadRequestException("Product name cannot empty");
        }

        if(product.getPrice() == null || product.getProductName().isEmpty()) {
            throw new ApiBadRequestException("Product price cannot empty");
        }

        if(product.getQuantity() == null) {
            throw new ApiBadRequestException("Product quantity cannot empty");
        }

        Product saveProduct = productRepository.save(new Product(null, product.getProductName(), product.getPrice(), product.getQuantity()));

        return new ProductResponse(saveProduct.getProductId(), saveProduct.getProductName(), saveProduct.getPrice(), saveProduct.getQuantity());
    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(new ProductResponse(product.getProductId(), product.getProductName(), product.getPrice(), product.getQuantity()));
        }
        return productResponses;
    }

    public ProductResponse getProduct(Long productID) {
        Product product = productRepository.findById(productID).orElseThrow(
                () -> new ApiNotFoundException("Not Found product with id: " + productID)
        );
        return new ProductResponse(product.getProductId(), product.getProductName(), product.getPrice(), product.getQuantity());
    }

    @Transactional
    public ProductResponse updateProduct(Long productID, ProductRequest productRequest) {
        Product product = productRepository.findById(productID).orElseThrow(
                () -> new ApiNotFoundException("Not Found product with id: " + productID)
        );

        if((productRequest.getProductName() != null)
                && product.getProductName() != productRequest.getProductName()) {
            if(!productRequest.getProductName().isEmpty()) {
                product.setProductName(productRequest.getProductName());
            }
        }

        if((productRequest.getPrice() != null)
                && product.getPrice() != productRequest.getPrice()) {
            if (!productRequest.getPrice().isEmpty()) {
                product.setPrice(productRequest.getPrice());
            }
        }

        if((productRequest.getQuantity() != null)
                && product.getQuantity() != productRequest.getQuantity()) {
            product.setQuantity(productRequest.getQuantity());
        }

        return new ProductResponse(product.getProductId(), product.getProductName(), product.getPrice(), product.getQuantity());
    }

    public void deleteProduct(Long productID) {
        Product product = productRepository.findById(productID).orElseThrow(
                () -> new ApiNotFoundException("Not found product with id: " + productID)
        );
        productRepository.delete(product);
    }
}
