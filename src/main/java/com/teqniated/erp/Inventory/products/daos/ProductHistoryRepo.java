package com.teqniated.erp.Inventory.products.daos;

import com.teqniated.erp.Inventory.products.model.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHistoryRepo extends JpaRepository<ProductHistory, Long> {
}
