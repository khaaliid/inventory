package com.teqniated.erp.Inventory.user.config;

import com.teqniated.erp.Inventory.user.baos.RoleService;
import com.teqniated.erp.Inventory.user.baos.UserService;
import com.teqniated.erp.Inventory.user.model.Role;
import com.teqniated.erp.Inventory.user.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer implements ApplicationRunner {

    private Logger logger = LogManager.getLogger(Initializer.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info("**************** DB Initialization **************************** ");


        addrole("ADMIN");
        addrole("USER");
        addUser("admin@teqnia.com","system","admin","admin",1);


        logger.info("**************** DB Initialization  END************************ ");
    }

    private void addUser(String userName, String firstName, String lastName, String password , int active) {

        User user= userService.findUserByEmail(userName);
        if(user==null) {
            user = new User();
            user.setActive(active);
            user.setPassword(password);
            user.setEmail(userName);
            user.setLastName(lastName);
            user.setName(firstName);
            userService.saveUser(user);
        }else{
            logger.info("User Already exists");
        }
    }

    private void addrole(String roleName) {
        Role role = roleService.findByRole(roleName);
        if(role==null) {
            role=new Role();
            role.setRole(roleName);
            roleService.saveRole(role);
        }else{
            logger.debug("Role ["+roleName+"] already exists !");
        }
    }
}
