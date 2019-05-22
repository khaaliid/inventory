package com.teqniated.erp.Inventory.products.controllers;

import com.teqniated.erp.Inventory.commons.utils.Constants;
import com.teqniated.erp.Inventory.products.baos.ProductService;
import com.teqniated.erp.Inventory.products.model.Product;
import com.teqniated.erp.Inventory.products.model.ProductHistory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {

    private final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
                                                                                                            private ProductService produtService;

    @RequestMapping(value="/user/home/{page}", method = RequestMethod.GET)
    public ModelAndView getPaginatedProducts(@PathVariable("page") Integer pageNo){
        ModelAndView modelAndView = new ModelAndView();
        PageRequest pageable = PageRequest.of(pageNo==null?0:(pageNo-1), Constants.PAGE_SIZE);
        Page<Product> productPage = produtService.getAllAvailableProduct(pageable);
        int totalPages = productPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("currentPageNumber", pageNo);

        List<Product> productList = productPage.getContent();
        logger.info("No of products returned : "+ (productList==null?0:productList.size()));
        modelAndView.addObject("productList",productList );


        modelAndView.setViewName("user/home");
        return modelAndView;
    }


    @RequestMapping(value="/admin/productControl", method = RequestMethod.POST)
    public ModelAndView addOrUpdateProduct(@Valid Product product, @RequestParam(value="productImageFile", required = false) MultipartFile productImage, BindingResult bindingResult){
       logger.info("Product ID : "+ product.getProductId());
        ProductHistory productHistory=null;
        ModelAndView modelAndView = new ModelAndView();
//            bindingResult
//                    .rejectValue("productName", "error.product",
//                            "There is already a user registered with the email provided");

        if (product.getProductId()==0 && (productImage==null || productImage.isEmpty())) {
           logger.info("No file uploaded .. product image will be replaced with the default image !");
           product.setProductImage("product.png");
        }else if(product.getProductId()!=0 && (productImage==null || productImage.isEmpty())){
            logger.info("No file uploaded .. product image will be replaced with the original image !");
            product.setProductImage(product.getOriginalProductImage());
        }else{

            product.setProductImage(uploadFile(productImage));
        }

            int originalQuantity = product.getOriginalQuantity();
            int updatedQuantity = product.getProductQuantity();
            int noOfProducts =updatedQuantity-originalQuantity;
            logger.debug("no Of existing : "+originalQuantity+" - updated quantity : "+updatedQuantity+" difference : "+noOfProducts);
        if(noOfProducts!=0){
            logger.info("product difference is :"+noOfProducts);
            productHistory = new ProductHistory();
            productHistory.setCreationDate(new Date());
            productHistory.setPrice(product.getProductPrice());
            productHistory.setProductId(product.getProductId());
            productHistory.setQuantity(noOfProducts);

            Set<ProductHistory> history = product.getProductHistory();
            if(history==null){
                history = new HashSet<ProductHistory>();
                history.add(productHistory);
            }
            product.setProductHistory(history);
        }
        if(product.getProductId()!=0) {
            product = produtService.addorUpdateProduct(product);
        }else{

            product.setProductHistory(null);
            product = produtService.addorUpdateProduct(product);
            productHistory.setProductId(product.getProductId());
            productHistory = produtService.saveProductHistory(productHistory);
            if(productHistory!=null){
                logger.debug("Product loged successfully !");
            }else{
                logger.warn("The Product not logged in DB !!!");
            }
        }
        if(product!=null ) {

            modelAndView.addObject("successMessage", "product has been saved successfully");

        }else{

            modelAndView.addObject("successMessage", "Error while adding product");

        }
        product.setOriginalQuantity(product.getProductQuantity());
        product.setOriginalProductImage(product.getProductImage());
        modelAndView.addObject("product",product);
        modelAndView.setViewName("admin/productControl");
        return modelAndView;
    }

    private String uploadFile(MultipartFile productImage) {
        String completeFileName=null;
        try {

            // Get the file and save it somewhere
            byte[] bytes = productImage.getBytes();
            completeFileName = System.currentTimeMillis()+productImage.getOriginalFilename();
            Path path = Paths.get(Constants.UPLOADED_FOLDER+completeFileName);

            Files.write(path, bytes);



        } catch (IOException e) {
           logger.error("Error while uploading file !",e);
        }

        return completeFileName;
    }

    @RequestMapping(value={"/admin/productControl","/admin/productControl/{productId}"}, method = RequestMethod.GET)
    public ModelAndView productManager(@PathVariable(value="productId") Optional<Integer> productId){
        ModelAndView modelAndView = new ModelAndView();
        logger.info(" forwarding to add product page");
        Product product =null;
        if(productId.isPresent() && productId !=null &&  productId.get()>0) {
             product = produtService.getProductById(productId.get());
             product.setOriginalQuantity(product.getProductQuantity());
             product.setOriginalProductImage(product.getProductImage());
//            product.setProductId(productId);
        }else{
            product=new Product();
        }
        logger.debug("product original quantity : "+product.getOriginalQuantity());

        Set<ProductHistory> productLogs = product.getProductHistory();
        if(productLogs!=null) {
            productLogs.forEach(productLog -> logger.debug("history record ID :" + productLog.getProductHistoryId()));
        }
        modelAndView.addObject("product",product);
        modelAndView.setViewName("admin/productControl");
        return modelAndView;
    }
}
