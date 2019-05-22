package com.teqniated.erp.Inventory.user.baos;

import com.teqniated.erp.Inventory.user.daos.RoleRepository;
import com.teqniated.erp.Inventory.user.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }
    public Role findByRole(String role){
        return roleRepository.findByRole(role);
    }
}
