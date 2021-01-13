package com.adriforczek.ecommerce.controllers;

import com.adriforczek.ecommerce.TestHelper;
import com.adriforczek.ecommerce.model.persistence.Cart;
import com.adriforczek.ecommerce.model.persistence.Item;
import com.adriforczek.ecommerce.model.persistence.User;
import com.adriforczek.ecommerce.model.persistence.repositories.CartRepository;
import com.adriforczek.ecommerce.model.persistence.repositories.ItemRepository;
import com.adriforczek.ecommerce.model.persistence.repositories.UserRepository;
import com.adriforczek.ecommerce.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestHelper.injectObject(cartController, "userRepository", userRepository);
        TestHelper.injectObject(cartController, "cartRepository", cartRepository);
        TestHelper.injectObject(cartController, "itemRepository", itemRepository);

        // create user
        User user = new User();
        user.setId(0);
        user.setUsername("adri");
        user.setPassword("securep@ssword");

        // create item
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> items = new ArrayList<>();
        items.add(item);

        // create cart
        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByUsername("adri")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("adri");
        request.setItemId(1L);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addToCart(request);
        Cart cart = response.getBody();

        assertNotNull(response);
        assertNotNull(cart);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(2.99), cart.getTotal());
    }

    @Test
    public void add_to_cart_invalid_user() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("nobody");
        request.setItemId(1L);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_invalid_item() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("adri");
        request.setItemId(2L);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() {
        // add items to cart
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("adri");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addToCart(request);
        Cart cart = response.getBody();

        assertNotNull(response);
        assertNotNull(cart);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_invalid_user() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(2L);
        request.setQuantity(1);
        request.setUsername("nobody");
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_invalid_item() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(2L);
        request.setQuantity(1);
        request.setUsername("adri");
        ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
