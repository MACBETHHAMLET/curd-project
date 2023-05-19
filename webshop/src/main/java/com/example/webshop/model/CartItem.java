package com.example.webshop.model;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CartItem {
    private long id;
    private int quantity;
}
