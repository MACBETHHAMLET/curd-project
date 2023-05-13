package org.project.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {
    private long id;
    private String name;
    private float weight;
    private int inStock;
    private float price;
    private String img;
}
