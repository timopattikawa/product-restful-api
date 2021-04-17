package com.kamoro.product.api.model;

import com.kamoro.product.api.entity.Product;

public class ProductRequest {

    private String productName;

    private String price;

    private Integer quantity;

    public ProductRequest(String productName, String price, Integer quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public Product productRequestToProduct() {
        return new Product(null, this.productName, this.price, this.quantity);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
