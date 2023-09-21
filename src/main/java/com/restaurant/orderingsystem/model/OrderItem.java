package com.restaurant.orderingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orderItems")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private BigDecimal totalPriceOfItem;

    @Column
    private Integer amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ElementCollection
    @CollectionTable(name="ADDITIONAL_VARIABLES", joinColumns = @JoinColumn(name = "ORDER_ITEM_ID"))
    @MapKeyColumn(name = "variable_name")
    @Column(name = "variable_value")
    private Map<String, String> additionalVariable;

}
