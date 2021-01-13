package com.adriforczek.ecommerce.controllers;

import java.util.List;

import com.adriforczek.ecommerce.model.persistence.repositories.ItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adriforczek.ecommerce.model.persistence.Item;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	public static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		if(items == null || items.isEmpty()) {
			log.info("There are no items returned for user {}", name);
			return ResponseEntity.notFound().build();
		} else {
			log.info("Items were successfully fetched for user {}", name);
			return ResponseEntity.ok(items);
		}
	}
	
}
