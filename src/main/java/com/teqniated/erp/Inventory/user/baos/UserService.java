package com.teqniated.erp.Inventory.user.baos;

import com.teqniated.erp.Inventory.user.model.Role;
import com.teqniated.erp.Inventory.user.model.User;
import com.teqniated.erp.Inventory.user.daos.RoleRepository;
import com.teqniated.erp.Inventory.user.daos.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    private  final Logger logger = LogManager.getLogger(UserService.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        if(userRole!=null) {

            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            user = userRepository.save(user);
        }else{
            logger.error("No Admin role found to add the Admin user -- User configuration will be skipped !!");
            user=null;
        }
        return user;
    }



}