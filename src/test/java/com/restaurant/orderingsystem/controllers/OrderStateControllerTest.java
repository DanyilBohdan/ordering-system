package com.restaurant.orderingsystem.controllers;

import com.restaurant.orderingsystem.exceptions.ConflictIllegalStateException;
import com.restaurant.orderingsystem.model.OrderState;
import com.restaurant.orderingsystem.model.requests.OrderItemsRequest;
import com.restaurant.orderingsystem.model.requests.OrderRequest;
import com.restaurant.orderingsystem.model.requests.OrderStateRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.restaurant.orderingsystem.model.OrderState.COMPLETED;
import static com.restaurant.orderingsystem.model.OrderState.IN_PROGRESS;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderStateControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @BeforeEach
    public void resetRepositories() {
        orderItemsRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    public void getAllOrderByIN_LINEStateTestWhenOrdersIsEmpty() {
        // execute
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(OrderState.IN_LINE);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertTrue(orders.getBody().isEmpty());
    }

    @Test
    public void getAllOrderByIN_PROGRESSStateTestWhenOrdersIsEmpty() {
        // execute
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(IN_PROGRESS);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertTrue(orders.getBody().isEmpty());
    }

    @Test
    public void getAllOrderByCOMPLETEDStateTestWhenOrdersIsEmpty() {
        // execute
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(OrderState.COMPLETED);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertTrue(orders.getBody().isEmpty());
    }

    @Test
    public void getAllOrderByIN_LINETest() {
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
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(OrderState.IN_LINE);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertEquals(2, orders.getBody().size());
    }

    @Test
    public void getAllOrderByIN_PROGRESSTest() {
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
        orderController.updateOrderState(orderIdResponse1.getBody(), OrderStateRequest.builder().orderState(IN_PROGRESS).build());
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(IN_PROGRESS);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertEquals(1, orders.getBody().size());
    }

    @Test
    public void getAllOrderByCOMPLETEDStateTest() {
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
        orderController.updateOrderState(orderIdResponse1.getBody(), OrderStateRequest.builder().orderState(IN_PROGRESS).build());
        orderController.updateOrderState(orderIdResponse1.getBody(), OrderStateRequest.builder().orderState(COMPLETED).build());
        ResponseEntity<List<OrderResponse>> orders = orderController.getAllOrders(COMPLETED);

        //verify
        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orders.getBody());
        Assertions.assertEquals(1, orders.getBody().size());
    }

    @Test
    public void updateOrderStateIsntCorrect() {
        // setup
        Long productId1 = 1L;
        OrderItemsRequest orderItem1 = OrderItemsRequest.builder()
                .productId(productId1)
                .amount(1)
                .additionalVariables(Map.of("isIceCubes", "true", "lemon", "false"))
                .build();
        OrderRequest orderRequest1 =
                OrderRequest.builder()
                        .orderItems(List.of(orderItem1))
                        .build();

        ResponseEntity<Long> orderIdResponse1 = orderController.createOrder(orderRequest1);

        Assertions.assertNotNull(orderIdResponse1);
        Assertions.assertTrue(orderIdResponse1.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(orderIdResponse1.getBody());

        // execute
        Assertions.assertThrows(ConflictIllegalStateException.class,
                () -> orderController.updateOrderState(orderIdResponse1.getBody(), OrderStateRequest.builder().orderState(COMPLETED).build()));
    }
}
