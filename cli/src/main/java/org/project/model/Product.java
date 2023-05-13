package org.project.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private String name;
    private float weight;
    private int inStock;
    private float price;
    private String img;

    public String toString() {
        return String.format("#%d %s  %s $  %s Kg  %d in stock  image file:%s", id, name, price, weight, inStock, img);
    }
}
