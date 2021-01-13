package com.adriforczek.ecommerce.controllers;

import com.adriforczek.ecommerce.TestHelper;
import com.adriforczek.ecommerce.model.persistence.Cart;
import com.adriforczek.ecommerce.model.persistence.User;
import com.adriforczek.ecommerce.model.persistence.repositories.CartRepository;
import com.adriforczek.ecommerce.model.persistence.repositories.UserRepository;
import com.adriforczek.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestHelper.injectObject(userController, "userRepository", userRepository);
        TestHelper.injectObject(userController, "cartRepository", cartRepository);
        TestHelper.injectObject(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("adri");
        user.setPassword("securep@ssword");
        user.setCart(cart);

        // stubbing
        when(userRepository.findByUsername("adri")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("nobody")).thenReturn(null);
        when(encoder.encode("testPassword")).thenReturn("hashedPassword");

    }

    @Test
    public void create_user_happy_path() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void create_user_confirm_password_mismatch() {
        when(encoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPass");

        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue()); // Bad response
    }

    @Test
    public void create_user_password_too_short() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("testUser");
        r.setPassword("pw");
        r.setConfirmPassword("pw");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name_happy_path() {
        final ResponseEntity<User> response = userController.findByUserName("adri");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("adri", u.getUsername());
    }

    @Test
    public void find_user_by_name_fail() {
        final ResponseEntity<User> response = userController.findByUserName("nobody");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue()); // Not Found
    }

    @Test
    public void find_user_by_id_happy_path() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());;
    }

    @Test
    public void find_user_by_id_fail() {
        final ResponseEntity<User> response = userController.findById(4L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
