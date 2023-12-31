package com.restaurant.orderingsystem.repositories;

import com.restaurant.orderingsystem.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
}
