package com.restaurant.orderingsystem.repositories;

import com.restaurant.orderingsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
