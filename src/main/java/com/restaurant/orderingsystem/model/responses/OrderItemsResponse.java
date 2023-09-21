package com.restaurant.orderingsystem.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsResponse {

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("additionalVariables")
    private Map<String, String> additionalVariables;

    @JsonProperty("amount")
    private Integer amount;
}
