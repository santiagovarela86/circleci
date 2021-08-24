package com.tacs.grupo1.covid.api.utils;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;
import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class BaseSecurityTest {

    public static RoleEntity mockRoleAdmin = null;
    public static RoleEntity mockRoleUser = null;
    public static RoleEntity mockRoleTelegram = null;

    public static PrivilegeEntity mockPrivilegeAdmin = null;
    public static PrivilegeEntity mockPrivilegeUser = null;
    public static PrivilegeEntity mokePrivilegeTelegram = null;

    public static UserEntity mockUser1 = null;
    public static UserEntity mockUser2 = null;
    public static UserEntity mockUser3 = null;


    public BaseSecurityTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init(){

        //PRIVILEGE GENERATION
        mockPrivilegeAdmin = new PrivilegeEntity().setId(1L).setName("ADMIN");
        mockPrivilegeUser = new PrivilegeEntity().setId(2L).setName("USER");
        mokePrivilegeTelegram = new PrivilegeEntity().setId(3L).setName("TELEGRAM");
        List<PrivilegeEntity> adminPrivileges = Arrays.asList(mockPrivilegeAdmin, mockPrivilegeUser);

        mockRoleAdmin = new RoleEntity().setId(1L).setName("ROLE_ADMIN").setPrivileges(adminPrivileges);
        mockRoleUser = new RoleEntity().setId(2L).setName("ROLE_USER").setPrivileges(Arrays.asList(mockPrivilegeUser));
        mockRoleTelegram = new RoleEntity().setId(3L).setName("ROLE_TELEGRAM").setPrivileges(Arrays.asList(mokePrivilegeTelegram));

        mockUser1 = new UserEntity()
                .setId(1L)
                .setUserName("Administrator")
                .setName("Juan")
                .setLastName("Perez")
                .setEmail("juanperez@aol.com")
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS")
                .setRoles(Arrays.asList(mockRoleAdmin));

        mockUser2 = new UserEntity()
                .setId(2L)
                .setUserName("Usuario1")
                .setName("Guillermo")
                .setLastName("Francella")
                .setEmail("guillermofrancella@sion.com")
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS")
                .setRoles(Arrays.asList(mockRoleUser));

        mockUser3 = new UserEntity()
                .setId(3L)
                .setUserName("TelegramBot")
                .setName("TelegramBot")
                .setLastName("TelegramBot")
                .setEmail("TelegramBot")
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS")
                .setRoles(Arrays.asList(mockRoleTelegram));

        mockRoleAdmin.setUsers(Arrays.asList(mockUser1));
        mockRoleUser.setUsers(Arrays.asList(mockUser2));
        mockRoleTelegram.setUsers(Arrays.asList(mockUser3));

        mockPrivilegeAdmin.setRoles(Arrays.asList(mockRoleAdmin));
        mockPrivilegeUser.setRoles(Arrays.asList(mockRoleUser));
        mokePrivilegeTelegram.setRoles(Arrays.asList(mockRoleTelegram));

        System.out.println("Carga de datos iniciales");

    }


}
