package com.example.webshop.controller;

import com.example.webshop.repo.CartRepo;
import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("cart", cartRepo.findAll());
        float total = cartRepo.findAll().stream().map(c -> c.getQuantity() * c.getProduct().getPrice()).reduce(0f, Float::sum);
        model.addAttribute("total", String.format("%.2f", total) );
        return "MainPage.html";
    }
}
