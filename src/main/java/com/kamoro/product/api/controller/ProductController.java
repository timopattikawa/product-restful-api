package com.kamoro.product.api.controller;

import com.kamoro.product.api.entity.Product;
import com.kamoro.product.api.model.ProductRequest;
import com.kamoro.product.api.model.ProductResponse;
import com.kamoro.product.api.model.WebResponse;
import com.kamoro.product.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    public WebResponse<ProductResponse> createProduct(@RequestBody ProductRequest product) {
        ProductResponse productResponse = productService.createNewProduct(product);

        return new WebResponse<ProductResponse>(
            200,
                "OK",
                productResponse
        );
    }

    @GetMapping(
            produces = "application/json"
    )
    public WebResponse<List<ProductResponse>> getAllProduct() {
        List<ProductResponse> productsResponse = productService.getAllProduct();
        return new WebResponse<>(200, "OK", productsResponse);
    }

    @GetMapping(path = "/{productID}")
    public WebResponse<ProductResponse> getProduct(@PathVariable("productID") Long productID) {
        ProductResponse productResponse = productService.getProduct(productID);
        return new WebResponse<>(200, "OK", productResponse);
    }


}
