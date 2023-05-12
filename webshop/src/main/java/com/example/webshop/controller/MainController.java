package com.example.webshop.controller;

import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "MainPage.html";
    }
}
