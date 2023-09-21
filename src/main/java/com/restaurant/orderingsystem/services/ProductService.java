package com.restaurant.orderingsystem.services;


import com.restaurant.orderingsystem.model.Product;
import com.restaurant.orderingsystem.model.requests.ProductRequest;

import java.util.List;

public interface ProductService {

    Product getProductById(Long id);

    List<Product> getProducts();

    Long createProduct(ProductRequest productRequest);

}
