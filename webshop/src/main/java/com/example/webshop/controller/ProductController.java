package com.example.webshop.controller;

import com.example.webshop.model.CartItem;
import com.example.webshop.model.Product;
import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/api/product")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            return ResponseEntity.ok(productRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productRepo.findById(id);
            return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/product")
    public ResponseEntity<Product> addProduct(@RequestBody Product newProduct) {
        try {
            Product product = productRepo.save(newProduct);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/api/product/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {
            Optional<Product> optionalProduct = productRepo.findById(id);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                BeanUtils.copyProperties(updatedProduct, product, "id"); // ignore id property
                productRepo.save(product);
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<HttpStatus> delProductById(@PathVariable Long id) {
        try {
            if (productRepo.findById(id).isPresent()){
                productRepo.deleteById(id);
                return ResponseEntity.ok().build();
            }else return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/product/purchase")
    // TODO: we need better error handling here...
    public ResponseEntity<HttpStatus> updateProducts(@RequestBody List<CartItem> items){
        boolean areAllItemsValid =  items.stream().allMatch(item -> {
            Optional<Product> optionalProduct = productRepo.findById(item.getId());
            return optionalProduct.filter(product -> item.getQuantity() <= product.getInStock() && item.getQuantity() >= 0).isPresent();
        });
        if (areAllItemsValid){
            // update the inStock properties of the products in the shop list.
            items.forEach(item -> {
                Optional<Product> optionalProduct = productRepo.findById(item.getId());
                assert optionalProduct.isPresent();
                Product p = optionalProduct.get();
                p.updateInStock(item.getQuantity());
                productRepo.save(p);
            });
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}
