package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.TelegramUser;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.repositories.RoleRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import com.tacs.grupo1.covid.api.utils.BaseSecurityTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tacs.grupo1.covid.api.utils.CountryUtils.mockCountryEntity;
import static com.tacs.grupo1.covid.api.utils.CountryUtils.mockCountryRepositoryGetAll;
import static com.tacs.grupo1.covid.api.utils.UserUtils.mockUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DefaultRolServiceTest extends BaseSecurityTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    DefaultRoleService roleService;

    @Test
    @DisplayName("save_Response_SUCCESS")
    public void validate_save_SUCCESS() {

        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRoleUser);

        RoleEntity roleEntity = roleService.save(new RoleEntity());

        verify(roleRepository,times(1)).save(Mockito.any(RoleEntity.class));

        System.out.println("-- roleEntity.getId() = " + roleEntity.getId());
        assertEquals(mockRoleUser.getId(), roleEntity.getId());
        System.out.println("-- roleEntity.getName() = " + roleEntity.getName());
        assertEquals(mockRoleUser.getName(), roleEntity.getName());
    }

    @Test
    @DisplayName("get_byName_Response_NotFoundException")
    public void validate_get_byName_NotFoundException() {
        when(roleRepository.getByName(anyString())).thenThrow(NotFoundException.class);
        assertThrows(
                NotFoundException.class,
                () -> roleService.get(anyString())
        );
    }

    @Test
    @DisplayName("get_byName_Response_SUCCESS")
    public void validate_get_byName_SUCCESS() {

        when(roleRepository.getByName(mockRoleUser.getName())).thenReturn(Optional.of(mockRoleUser));

        RoleEntity res = roleService.get(mockRoleUser.getName());

        System.out.println("---- res.getName() = " + res.getName());
        assertEquals(mockRoleUser.getName(), res.getName());

    }

    @Test
    @DisplayName("get_byId_Response_NotFoundException")
    public void validate_get_byId_NotFoundException() {
        when(roleRepository.getById(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(
                NotFoundException.class,
                () -> roleService.get(anyLong())
        );
    }

    @Test
    @DisplayName("get_byId_Response_SUCCESS")
    public void validate_get_byId_SUCCESS() {

        when(roleRepository.getById(mockRoleUser.getId())).thenReturn(Optional.of(mockRoleUser));

        RoleEntity res = roleService.get(mockRoleUser.getId());

        System.out.println("---- res.getId() = " + res.getId());
        assertEquals(mockRoleUser.getId(), res.getId());

    }

    @Test
    @DisplayName("update_Response_SUCCESS")
    public void update_Response_SUCCESS() {
        when(roleRepository.getById(mockRoleUser.getId())).thenReturn(Optional.of(mockRoleUser));
//        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRoleUser.setName("Saved"));
        roleService.update(2L,mockRoleUser);

        verify(roleRepository).save(mockRoleUser);

    }

    @Test
    @DisplayName("delete_Response_SUCCESS")
    public void delete_Response_SUCCESS() {
        when(roleRepository.getById(mockRoleUser.getId())).thenReturn(Optional.of(mockRoleUser));
//        when(roleRepository.save(any(RoleEntity.class))).thenReturn(mockRoleUser.setName("Saved"));
        roleService.delete(2L);

        verify(roleRepository).delete(mockRoleUser);

    }

    @Test
    @DisplayName("getList_Response_SUCCESS")
    public void getList_Response_SUCCESS() {

        ArrayList<RoleEntity> list = new ArrayList<RoleEntity>();
        list.add(mockRoleAdmin);
        list.add(mockRoleUser);
        list.add(mockRoleTelegram);

        when(roleRepository.findAll()).thenReturn(list);

        List<RoleEntity> listRes =  roleService.getList();
        System.out.println("-- listRes.size() = " + listRes.size());
        assertEquals(list.size(),listRes.size());

    }

}
