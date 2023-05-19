package com.example.webshop.controller;

import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "MainPage.html";
    }
    @PostMapping("/purchase")
    public ResponseEntity<String> handlePurchase(@RequestBody List<PurchaseItem> purchaseList, @RequestParam("userId") String userId) {
        // Verify the purchase list
        boolean isPurchaseValid = verifyPurchase(purchaseList);

        if (isPurchaseValid) {
            // Update the database with the purchase
            updateDatabase(userId, purchaseList);

            // Notify the user of successful purchase
            notifyUser(userId);

            return ResponseEntity.ok("Purchase was successful!");
        } else {
            return ResponseEntity.badRequest().body("Invalid purchase list.");
        }
    }

    private boolean verifyPurchase(List<PurchaseItem> purchaseList) {
        // Implement your logic to verify the purchase list
        // Return true if the purchase is valid, false otherwise
        // Add your implementation here
    }

    private void updateDatabase(String userId, List<PurchaseItem> purchaseList) {
        // Implement your logic to update the database with the user's purchase
        // Use the userId and purchaseList to update the appropriate records in your database

        // Example code using PurchaseRepository
        for (PurchaseItem purchaseItem : purchaseList) {
            Purchase purchase = new Purchase(userId, purchaseItem);
            purchaseRepository.save(purchase);
        }
    }

    private void notifyUser(String userId) {
        // Implement your logic to notify the user about the successful purchase
        // Use the userId to fetch the user's contact information from the database
        // Then, send a notification to the user via email, SMS, or any other preferred method
        // Add your implementation here

        // Example code using NotificationService
        notificationService.sendNotification(userId, "Your purchase was successful!");
    }
}
