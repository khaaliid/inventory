package com.teqniated.erp.Inventory.products.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="product_logs")
public class ProductHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_log_id")
    private int productHistoryId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;



}
