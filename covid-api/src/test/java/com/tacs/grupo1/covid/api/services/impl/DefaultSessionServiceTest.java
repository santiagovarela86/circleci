package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;
import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.exceptions.SignUpException;
import com.tacs.grupo1.covid.api.repositories.PrivilegeRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import com.tacs.grupo1.covid.api.utils.BaseSecurityTest;
import com.tacs.grupo1.covid.api.websecurity.JwtRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DefaultSessionServiceTest extends BaseSecurityTest {

    @Mock
    UserRepository userRepository;

    @Mock
    DefaultRoleService roleService;

    @Autowired
    @InjectMocks
    DefaultSessionService sessionService;

    @Test
    @DisplayName("signUp_Response_SUCCESS")
    public void validate_signUp_SUCCESS() {

        when(roleService.get("ROLE_USER")).thenReturn(mockRoleUser.setUsers(new ArrayList<UserEntity>()));
        when(roleService.save(any(RoleEntity.class))).thenReturn(mockRoleUser);
        when(userRepository.save(mockUser2)).thenReturn(mockUser1);

        UserEntity res = sessionService.signUp(mockUser2);

        System.out.println("TERMINO SIGNUP: " + res.getUserName());

    }

    @Test
    @DisplayName("signUp_Response_SignUpException_ERR1")
    public void validate_signUp_SignUpException_ERR1() {

        assertThrows(
                SignUpException.class,
                () -> sessionService.signUp(new UserEntity())
        );
    }

    @Test
    @DisplayName("signUp_Response_SignUpException_ERR2")
    public void validate_signUp_SignUpException_ERR2() {
        when(userRepository.getByEmail(mockUser2.getEmail())).thenReturn(Optional.of(mockUser2));
        assertThrows(
                SignUpException.class,
                () -> sessionService.signUp(mockUser2)
        );
    }

    @Test
    @DisplayName("signUp_Response_SignUpException_ERR3")
    public void validate_signUp_SignUpException_ERR3() {
        when(userRepository.getByEmail(mockUser2.getEmail())).thenReturn(Optional.empty());
        when(userRepository.getByUserName(mockUser2.getUserName())).thenReturn(Optional.of(mockUser2));
        assertThrows(
                SignUpException.class,
                () -> sessionService.signUp(mockUser2)
        );
    }

}
