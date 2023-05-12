package com.example.webshop.controller;

import com.example.webshop.model.Product;
import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/admin/product/")
    public String adminPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "AdminProducts.html";
    }

    @GetMapping("/admin/product/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "NewProductForm.html";
    }

    @PostMapping("/admin/product/new")
    public String addNewProduct(@Validated Product newProduct) {
        productRepo.save(newProduct);
        return "redirect:/admin/product/";

    }

    @GetMapping("/admin/product/{id}/")
    public String editProduct(@PathVariable Long id, Model model) {

        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "ProductEditForm.html";
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");

    }

    @PostMapping("/admin/product/{id}")
    public String editProduct(@PathVariable Long id, @Validated Product edittedProduct) {
        try {
            Optional<Product> optionalProduct = productRepo.findById(id);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                if (edittedProduct.getImg() == "")
                    BeanUtils.copyProperties(edittedProduct, product, "id", "img"); // ignore id property
                else
                    BeanUtils.copyProperties(edittedProduct, product, "id"); // ignore id property
                productRepo.save(product);
                return "redirect:/admin/product/";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops, Something went wrong on our side; Please contact support");
        }

    }

    @GetMapping("/admin/product/{id}/del")
    public String delProduct(@PathVariable Long id) {
        try {
            productRepo.deleteById(id);
        } catch (Exception ignored) {
        }
        return "redirect:/admin/product/";
    }
}
