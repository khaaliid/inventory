package com.teqniated.erp.Inventory.products.baos;

import com.teqniated.erp.Inventory.products.daos.ProductHistoryRepo;
import com.teqniated.erp.Inventory.products.daos.ProductRepo;
import com.teqniated.erp.Inventory.products.model.Product;
import com.teqniated.erp.Inventory.products.model.ProductHistory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductHistoryRepo productHistoryRepo;

    private final Logger logger = LogManager.getLogger(ProductService.class);

    public List<Product> getAllAvailableProduct(){

        return productRepo.findAll();

    }

    public Page<Product> getAllAvailableProduct(PageRequest page){

        return productRepo.findAll(page);

    }

    public Product addorUpdateProduct(Product product) {
        Product resultProduct =null;
        if(product!=null) {
            logger.debug("Before adding product ["+product.getProductId()+"] with history obj size :" + (product.getProductHistory()==null?0:product.getProductHistory().size()));
            resultProduct = productRepo.save(product);
        }
       return resultProduct;
    }

    public Product getProductById(Integer productId) {

        return productRepo.findById(new Long(productId)).get();
    }

    public ProductHistory saveProductHistory(ProductHistory productHistory) {
        return productHistoryRepo.save(productHistory);
    }
}
