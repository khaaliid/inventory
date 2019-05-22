package com.teqniated.erp.Inventory.user.controller;

import javax.validation.Valid;

import com.teqniated.erp.Inventory.commons.utils.Constants;
import com.teqniated.erp.Inventory.products.baos.ProductService;
import com.teqniated.erp.Inventory.products.model.Product;
import com.teqniated.erp.Inventory.user.model.User;
import com.teqniated.erp.Inventory.user.baos.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class LoginController {

    private  final Logger logger = LogManager.getLogger(LoginController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private ProductService produtService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error){
        ModelAndView modelAndView = new ModelAndView();
        logger.info("try to login  -- Error message : "+error);
        if (error != null) {
            logger.debug("Error page will return !");
            modelAndView.setViewName("login");
        } else{

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug("No Error - Auth object [ "+auth+"]");

            if (!(auth instanceof AnonymousAuthenticationToken)) {
                logger.debug("Already logged in !");
                /* The user is logged in :) */
                modelAndView.setViewName("user/home");
            }else {
                logger.debug("New user to login!");
                modelAndView.setViewName("login");
            }
        }


        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }


    @RequestMapping(value="/user/home", method = RequestMethod.GET)
    public ModelAndView userHome(){
        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());
//        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

        PageRequest pageable = PageRequest.of(0, Constants.PAGE_SIZE);
        Page<Product> productPage = produtService.getAllAvailableProduct(pageable);
        int totalPages = productPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("activeArticleList", true);
        List<Product> productList = productPage.getContent();
        logger.info("No of products returned : "+ (productList==null?0:productList.size()));
        modelAndView.addObject("productList",productList );


        modelAndView.setViewName("user/home");
        return modelAndView;
    }


}