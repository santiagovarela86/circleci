package com.tacs.grupo1.covid.api.util;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.User;
import com.tacs.grupo1.covid.api.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConvertUser {

    private final RoleService roleService;

    @Autowired
    public ConvertUser(RoleService roleService) {
        this.roleService = roleService;
    }

    public UserEntity dtoToEntity(User user) {
        return new UserEntity()
                .setId(user.getId())
                .setUserName(user.getUserName())
                .setPassword(user.getPassword())
                .setName(user.getName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setLastLogin(user.getLastLogin());
                //.setRoles(Arrays.asList(roleService.get(user.getRole())));
    }

    public User entityToDto(UserEntity user) {
        return new User()
                .setId(user.getId())
                .setUserName(user.getUserName())
                .setPassword("*Protected*")
                .setName(user.getName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setLastLogin(user.getLastLogin());
                //.setRole(user.getRoles().stream().findFirst().orElseThrow(NotFoundException::new).getName());
    }
}
