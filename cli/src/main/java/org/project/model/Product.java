package org.project.model;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private String name;
    private float weight;
    private int inStock;
    private float price;
    private String img;
}
