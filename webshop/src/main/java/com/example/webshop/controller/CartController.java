package com.example.webshop.controller;

import com.example.webshop.model.CartItem;
import com.example.webshop.repo.CartRepo;
import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CartController {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @PostMapping("/api/cart")
    public ResponseEntity<CartItem> addCart(@RequestBody CartItem cartItem) {
        try {
            return ResponseEntity.ok(cartRepo.save(cartItem));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/purchase")
    public ResponseEntity<String> purchase() {
        boolean valid = cartRepo.findAll().stream().allMatch(item -> item.getQuantity() <= item.getProduct().getInStock() && item.getQuantity() > 0);
        if (valid) {
            cartRepo.findAll().forEach(item -> {
                productRepo.save(item.getProduct().updateInStock(item.getQuantity()));
            });
            cartRepo.deleteAll();
            return new ResponseEntity<String>("your purchase was successful ☺️", HttpStatus.OK);
        } else
            return new ResponseEntity<String>("sorry 🥲;we don't have the requested quantities available in stock", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/cart/{id}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long id, @RequestBody CartItem updatedCartItem) {
        try {
            Optional<CartItem> optionalCartItem = cartRepo.findById(id);
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                cartItem.setQuantity(updatedCartItem.getQuantity());
                cartRepo.save(cartItem);
                return ResponseEntity.ok(cartItem);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/api/cart/{id}")
    public ResponseEntity<HttpStatus> removeCart(@PathVariable Long id) {
        try {
            if (cartRepo.findById(id).isPresent()) {
                cartRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } else return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
