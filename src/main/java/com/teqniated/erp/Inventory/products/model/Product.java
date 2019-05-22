package com.teqniated.erp.Inventory.products.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="inv_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private long productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_image")
    private String productImage;

    @Transient
    private String originalProductImage;

    @Column(name="product_quantity")
    private int productQuantity;

    @Transient
    private int originalQuantity;

    @Column(name="product_price")
    private int productPrice;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Set<ProductHistory> productHistory;


}
