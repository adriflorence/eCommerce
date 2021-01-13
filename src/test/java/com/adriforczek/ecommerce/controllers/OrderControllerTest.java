package com.adriforczek.ecommerce.controllers;

import com.adriforczek.ecommerce.TestHelper;
import com.adriforczek.ecommerce.model.persistence.Cart;
import com.adriforczek.ecommerce.model.persistence.Item;
import com.adriforczek.ecommerce.model.persistence.User;
import com.adriforczek.ecommerce.model.persistence.UserOrder;
import com.adriforczek.ecommerce.model.persistence.repositories.OrderRepository;
import com.adriforczek.ecommerce.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestHelper.injectObject(orderController, "orderRepository", orderRepository);
        TestHelper.injectObject(orderController, "userRepository", userRepository);

        // create item
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> items = new ArrayList<>();
        items.add(item);

        // create user
        User user = new User();
        user.setId(0);
        user.setUsername("adri");
        user.setPassword("securep@ssword");

        // create cart
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99));
        user.setCart(cart);

        // create order
        UserOrder order = UserOrder.createFromCart(cart);
        List<UserOrder> orders = new ArrayList<>();
        orders.add(order);

        when(userRepository.findByUsername("adri")).thenReturn(user);
        when(userRepository.findByUsername("nobody")).thenReturn(null);
        when(orderRepository.findByUser(user)).thenReturn(orders);
    }

    // TEST SUBMIT

    @Test
    public void submit_order_happy_path() {
        ResponseEntity<UserOrder> response = orderController.submit("adri");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertEquals(1, order.getItems().size());
        assertEquals(BigDecimal.valueOf(2.99), order.getTotal());
    }

    @Test
    public void submit_order_fail() {
        ResponseEntity<UserOrder> response = orderController.submit("nobody");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue()); // Not Found
    }

    // TEST ORDERS FOR USER

    @Test
    public void get_orders_for_user_happy_path() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("adri");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals(BigDecimal.valueOf(2.99), orders.get(0).getTotal());
    }

    @Test
    public void get_orders_for_user_fail() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("nobody");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue()); // Not Found
    }
}
