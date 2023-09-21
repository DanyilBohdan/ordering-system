package com.restaurant.orderingsystem.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("price")
    @NotNull
    private BigDecimal price;

    @JsonProperty("cuisineId")
    @NotNull
    private Long cuisineId;

}


