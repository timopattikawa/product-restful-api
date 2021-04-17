package com.kamoro.product.api.repository;

import com.kamoro.product.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products WHERE product_name = ?1", nativeQuery = true)
    Optional<Product> findByName(String productName);
}
