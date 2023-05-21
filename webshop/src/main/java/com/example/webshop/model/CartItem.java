package com.example.webshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart-items")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private int quantity;
}
