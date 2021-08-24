package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;
import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.PrivilegeRepository;
import com.tacs.grupo1.covid.api.repositories.RoleRepository;
import com.tacs.grupo1.covid.api.utils.BaseSecurityTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DefaultPrivilegeServiceTest extends BaseSecurityTest {

    @Mock
    PrivilegeRepository privilegeRepository;

    @InjectMocks
    DefaultPrivilegeService privilegeService;

    @Test
    @DisplayName("save_Response_SUCCESS")
    public void validate_save_SUCCESS() {

        when(privilegeRepository.save(any(PrivilegeEntity.class))).thenReturn(mockPrivilegeUser);

        PrivilegeEntity privilegeEntity = privilegeService.save(new PrivilegeEntity());

        verify(privilegeRepository,times(1)).save(Mockito.any(PrivilegeEntity.class));

        System.out.println("-- privilegeEntity.getId() = " + privilegeEntity.getId());
        assertEquals(mockPrivilegeUser.getId(), privilegeEntity.getId());
        System.out.println("-- privilegeEntity.getName() = " + privilegeEntity.getName());
        assertEquals(mockPrivilegeUser.getName(), privilegeEntity.getName());
    }

    @Test
    @DisplayName("get_byName_Response_NotFoundException")
    public void validate_get_byName_NotFoundException() {
        when(privilegeRepository.getByName(anyString())).thenThrow(NotFoundException.class);
        assertThrows(
                NotFoundException.class,
                () -> privilegeService.get(anyString())
        );
    }


    @Test
    @DisplayName("get_byName_Response_SUCCESS")
    public void validate_get_byName_SUCCESS() {

        when(privilegeRepository.getByName(mockPrivilegeUser.getName())).thenReturn(Optional.of(mockPrivilegeUser));

        PrivilegeEntity res = privilegeService.get(mockPrivilegeUser.getName());

        System.out.println("---- res.getName() = " + res.getName());
        assertEquals(mockPrivilegeUser.getName(), res.getName());

    }

    @Test
    @DisplayName("get_byId_Response_NotFoundException")
    public void validate_get_byId_NotFoundException() {
        when(privilegeRepository.getById(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(
                NotFoundException.class,
                () -> privilegeService.get(anyLong())
        );
    }

    @Test
    @DisplayName("get_byId_Response_SUCCESS")
    public void validate_get_byId_SUCCESS() {

        when(privilegeRepository.getById(mockRoleUser.getId())).thenReturn(Optional.of(mockPrivilegeUser));

        PrivilegeEntity res = privilegeService.get(mockPrivilegeUser.getId());

        System.out.println("---- res.getId() = " + res.getId());
        assertEquals(mockRoleUser.getId(), res.getId());

    }

    @Test
    @DisplayName("update_Response_SUCCESS")
    public void update_Response_SUCCESS() {
        when(privilegeRepository.getById(mockPrivilegeUser.getId())).thenReturn(Optional.of(mockPrivilegeUser));

        privilegeService.update(2L,mockPrivilegeUser);

        verify(privilegeRepository).save(mockPrivilegeUser);

    }

    @Test
    @DisplayName("delete_Response_SUCCESS")
    public void delete_Response_SUCCESS() {
        when(privilegeRepository.getById(mockPrivilegeUser.getId())).thenReturn(Optional.of(mockPrivilegeUser));

        privilegeService.delete(2L);

        verify(privilegeRepository).delete(mockPrivilegeUser);

    }

    @Test
    @DisplayName("getList_Response_SUCCESS")
    public void getList_Response_SUCCESS() {

        ArrayList<PrivilegeEntity> list = new ArrayList<PrivilegeEntity>();
        list.add(mockPrivilegeAdmin);
        list.add(mockPrivilegeUser);


        when(privilegeRepository.findAll()).thenReturn(list);

        List<PrivilegeEntity> listRes =  privilegeService.getList();
        System.out.println("-- listRes.size() = " + listRes.size());
        assertEquals(list.size(),listRes.size());

    }

}
