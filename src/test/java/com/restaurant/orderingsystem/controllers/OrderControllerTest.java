package com.restaurant.orderingsystem.controllers;

import com.restaurant.orderingsystem.exceptions.NoOrderException;
import com.restaurant.orderingsystem.exceptions.NoProductException;
import com.restaurant.orderingsystem.model.requests.OrderItemsRequest;
import com.restaurant.orderingsystem.model.requests.OrderRequest;
import com.restaurant.orderingsystem.model.responses.OrderResponse;
import com.restaurant.orderingsystem.repositories.OrderItemsRepository;
import com.restaurant.orderingsystem.repositories.OrderRepository;
import com.restaurant.orderingsystem.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.restaurant.orderingsystem.model.OrderState.IN_LINE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @BeforeEach
    public void resetRepositories() {
        orderItemsRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    public void createOrderTestWhenNotFoundProduct() {
        // setup
        OrderItemsRequest orderItem = OrderItemsRequest.builder()
                .productId(99L)
                .amount(2)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem))
                        .build();

        // execute
        Assertions.assertThrows(NoProductException.class, () -> orderController.createOrder(orderRequest));

    }


    @Test
    public void createOrderWhenBodyDoesntHaveProductId() {
        // setup
        OrderItemsRequest orderItem = OrderItemsRequest.builder()
                .amount(2)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem))
                        .build();

        // execute
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> orderController.createOrder(orderRequest));

    }

    @Test
    public void createOrderWhenBodyDoesntHaveAmount() {
        // setup
        OrderItemsRequest orderItem = OrderItemsRequest.builder()
                .productId(1L)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem))
                        .build();

        // execute
        ResponseEntity<Long> orderIdResponse = orderController.createOrder(orderRequest);

        //verify

        Assertions.assertNotNull(orderIdResponse);
        Assertions.assertTrue(orderIdResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse.getBody());

        Assertions.assertEquals(1, orderRepository.findAll().size());
        Assertions.assertEquals(1, orderItemsRepository.findAll().size());

    }

    @Test
    public void createOrderTestWhenBodyIsCorrectResponseSuccessful() {
        // setup
        Long productId = 1L;
        OrderItemsRequest orderItem = OrderItemsRequest.builder()
                .productId(productId)
                .amount(2)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem))
                        .build();

        // execute
        ResponseEntity<Long> orderIdResponse = orderController.createOrder(orderRequest);

        //verify

        Assertions.assertNotNull(orderIdResponse);
        Assertions.assertTrue(orderIdResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse.getBody());
        Assertions.assertEquals(
                productRepository.findById(productId).get().getPrice().multiply(BigDecimal.valueOf(orderItem.getAmount())),
                orderRepository.findById(orderIdResponse.getBody()).get().getTotalPrice());

        Assertions.assertEquals(1, orderRepository.findAll().size());
        Assertions.assertEquals(1, orderItemsRepository.findAll().size());

    }

    @Test
    public void createOrderTestWhenBodyHasTwoOrdersResponseSuccessful() {
        // setup
        Long productId1 = 1L;
        OrderItemsRequest orderItem1 = OrderItemsRequest.builder()
                .productId(productId1)
                .amount(1)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();
        Long productId2 = 2L;
        OrderItemsRequest orderItem2 = OrderItemsRequest.builder()
                .productId(productId2)
                .amount(1)
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem1, orderItem2))
                        .build();

        // execute
        ResponseEntity<Long> orderIdResponse = orderController.createOrder(orderRequest);

        //verify

        Assertions.assertNotNull(orderIdResponse);
        Assertions.assertTrue(orderIdResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse.getBody());

        BigDecimal totalPriceOfFirstOrderItem = productRepository.findById(productId1).get().getPrice().multiply(BigDecimal.valueOf(orderItem1.getAmount()));
        BigDecimal totalPriceOfSecondOrderItem = productRepository.findById(productId2).get().getPrice().multiply(BigDecimal.valueOf(orderItem2.getAmount()));
        Assertions.assertEquals(
                totalPriceOfFirstOrderItem.add(totalPriceOfSecondOrderItem),
                orderRepository.findById(orderIdResponse.getBody()).get().getTotalPrice());

        Assertions.assertEquals(1, orderRepository.findAll().size());
        Assertions.assertEquals(2, orderItemsRepository.findAll().size());
    }

    @Test
    public void createOrderTestWhenBodyDosntHaveAdditionalVariablesResponseSuccessful() {
        // setup
        OrderItemsRequest orderItem = OrderItemsRequest.builder()
                .productId(1L)
                .amount(2)
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem))
                        .build();

        // execute
        ResponseEntity<Long> orderIdResponse = orderController.createOrder(orderRequest);

        //verify

        Assertions.assertNotNull(orderIdResponse);
        Assertions.assertTrue(orderIdResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse.getBody());

        Assertions.assertEquals(1, orderRepository.findAll().size());
        Assertions.assertEquals(1, orderItemsRepository.findAll().size());

    }

    @Test
    public void getOrderByIdTestWhenIdIsntCorrect() {
        // execute
        Assertions.assertThrows(NoOrderException.class, () -> orderController.getOrder(1L));
    }

    @Test
    public void getOrderByIdTestWhenIdIsCorrect() {
        // setup
        Long productId1 = 1L;
        OrderItemsRequest orderItem1 = OrderItemsRequest.builder()
                .productId(productId1)
                .amount(1)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();
        Long productId2 = 2L;
        OrderItemsRequest orderItem2 = OrderItemsRequest.builder()
                .productId(productId2)
                .amount(1)
                .build();

        OrderRequest orderRequest =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem1, orderItem2))
                        .build();

        ResponseEntity<Long> orderIdResponse = orderController.createOrder(orderRequest);

        Assertions.assertNotNull(orderIdResponse);
        Assertions.assertTrue(orderIdResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse.getBody());

        // execute
        ResponseEntity<OrderResponse> orderResponse = orderController.getOrder(orderIdResponse.getBody());

        //verify
        Assertions.assertNotNull(orderResponse);
        Assertions.assertTrue(orderResponse.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderResponse.getBody());
        Assertions.assertEquals(IN_LINE, orderResponse.getBody().getOrderState());
        Assertions.assertEquals(orderRequest.getOrderItems().size(), orderResponse.getBody().getOrderItems().size());

    }

    @Test
    public void getAllOrderTestWhenOrdersIsEmpty() {
        // execute
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(null);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertTrue(orders.getBody().isEmpty());
    }

    @Test
    public void getAllOrderTestWhenTwoOrders() {
        // setup
        Long productId1 = 1L;
        OrderItemsRequest orderItem1 = OrderItemsRequest.builder()
                .productId(productId1)
                .amount(1)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();
        Long productId2 = 2L;
        OrderItemsRequest orderItem2 = OrderItemsRequest.builder()
                .productId(productId2)
                .amount(1)
                .build();

        OrderRequest orderRequest1 =
                OrderRequest.builder()
                        .orderItems(List.of( orderItem2))
                        .build();
        OrderRequest orderRequest2 =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem1))
                        .build();

        ResponseEntity<Long> orderIdResponse1 = orderController.createOrder(orderRequest1);
        ResponseEntity<Long> orderIdResponse2 = orderController.createOrder(orderRequest2);

        Assertions.assertNotNull(orderIdResponse1);
        Assertions.assertTrue(orderIdResponse1.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse1.getBody());
        Assertions.assertNotNull(orderIdResponse2);
        Assertions.assertTrue(orderIdResponse2.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse2.getBody());


        // execute
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(null);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertEquals(2, orders.getBody().size());
    }
}
