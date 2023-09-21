package com.restaurant.orderingsystem.controllers;

import com.restaurant.orderingsystem.model.Product;
import com.restaurant.orderingsystem.model.requests.ProductRequest;
import com.restaurant.orderingsystem.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/product")
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @GetMapping(path = "/product/{id}")
    public ResponseEntity<Product> getProducts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping(path = "/products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }
}
