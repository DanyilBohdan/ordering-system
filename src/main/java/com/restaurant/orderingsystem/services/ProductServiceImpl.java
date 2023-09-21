package com.restaurant.orderingsystem.services;

import com.restaurant.orderingsystem.exceptions.NoProductException;
import com.restaurant.orderingsystem.model.Cuisine;
import com.restaurant.orderingsystem.model.Product;
import com.restaurant.orderingsystem.model.requests.ProductRequest;
import com.restaurant.orderingsystem.repositories.CuisineRepository;
import com.restaurant.orderingsystem.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Override
    @Transactional
    public Product getProductById(Long id) {
       return productRepository.findById(id).orElseThrow(() -> new NoProductException("Not found product with id = " + id));
    }
    @Override
    @Transactional
    public List<Product> getProducts() {
       return productRepository.findAll();
    }

    @Override
    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Cuisine cuisine = cuisineRepository.findById(productRequest.getCuisineId())
                .orElseThrow(() -> new NoProductException("Not found cuisine with id = " + productRequest.getCuisineId()));
        Product product = Product.builder()
                .name(productRequest.getName())
                .cuisine(cuisine)
                .price(productRequest.getPrice())
                .build();
        return productRepository.save(product).getId();
    }
}
