package com.teqniated.erp.Inventory.products.daos;

import com.teqniated.erp.Inventory.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
    public Page<Product> findAll(Pageable pageable);
}
