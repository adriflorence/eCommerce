package com.adriforczek.ecommerce.controllers;

import com.adriforczek.ecommerce.TestHelper;
import com.adriforczek.ecommerce.model.persistence.Item;
import com.adriforczek.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestHelper.injectObject(itemController, "itemRepository", itemRepository);

        // create item
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("Round Widget")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void get_all_items_happy_path() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> items = response.getBody();

        assertNotNull(response);
        assertNotNull(items);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, items.size());
    }

    @Test
    public void get_item_by_id_happy_path() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Item item = response.getBody();

        assertNotNull(response);
        assertNotNull(item);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_item_by_id_fail() {
        ResponseEntity<Item> response = itemController.getItemById(2L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_happy_path() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
        List<Item> items = response.getBody();

        assertNotNull(response);
        assertNotNull(items);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, items.size());
    }

    @Test
    public void get_items_by_name_fail() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
