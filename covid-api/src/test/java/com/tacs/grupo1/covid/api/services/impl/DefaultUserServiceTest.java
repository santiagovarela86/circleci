package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.TelegramUser;
import com.tacs.grupo1.covid.api.dto.User;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import com.tacs.grupo1.covid.api.util.ConvertUser;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.SessionService;
import com.tacs.grupo1.covid.api.services.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static com.tacs.grupo1.covid.api.utils.CountryListUtils.mockCountryListEntity;
import static com.tacs.grupo1.covid.api.utils.CountryUtils.mockCountryEntity;
import static com.tacs.grupo1.covid.api.utils.UserUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultUserServiceTest extends BaseTest {
    @Mock
    ConvertUser convertUser;

    @Mock
    UserRepository userRepository;

    @Mock
    CountryListRepository countryListRepository;

    @Mock
    DefaultSessionService sessionService;

    @InjectMocks
    DefaultUserService userService;


    @Test
    @DisplayName("When repository respond ok, then service return same object")
    void whenGetUserByIdReturnOk() {
        UserEntity userEntity = mockUserEntity().setId(USER_ID).setName(USER_NAME);
        mockUserRepositoryGetAll(userRepository, userEntity);

        UserEntity user = userService.get(USER_ID);

        assertEquals(userEntity, user);
        verify(userRepository).getById(USER_ID);
    }

    @Nested
    @DisplayName("The service throw an exception")
    class ServiceThrowException {
        @Test
        @DisplayName("In getById method when repository throw NotFoundException")
        void inGetByIdWhenRepositoryThrowNotFoundException() {
            mockUserRepositoryGetByIdThrow(userRepository, NotFoundException.class);

            assertThrows(
                    NotFoundException.class,
                    () -> userService.get(USER_ID)
            );
        }

        @Test
        @DisplayName("In delete method when repository throw NotFoundException")
        void inDeleteWhenRepositoryThrowNotFoundException() {
            mockUserRepositoryGetByIdThrow(userRepository, NotFoundException.class);

            assertThrows(
                    NotFoundException.class,
                    () -> userService.delete(USER_ID)
            );
        }
    }

    @Test
    @DisplayName("When repository saves an user then return same user")
    public void saveUserReturnUser() {
        UserEntity userEntity = mockUserEntity().setId(USER_ID).setName(USER_NAME);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        UserEntity returnedUser = userRepository.save(new UserEntity());

        verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
        assertEquals(userEntity.getId(), returnedUser.getId());
    }

    @Test
    @DisplayName("In get telegram id when repository throw NotFoundException")
    public void validateTelegramIdThrowsNotFoundException() {
        when(userRepository.getByTelegramId(anyLong())).thenThrow(new NotFoundException());

        assertThrows(
                NotFoundException.class,
                () -> userService.validateTelegramId(anyLong())
        );
    }

    @Test
    @DisplayName("User getList should return expected list")
    public void getListShouldReturnExpectedList() {
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity().setId(1L).setName("juan").setLastName("perez").setUserName("jperez"));
        users.add(new UserEntity().setId(2L).setName("peter").setLastName("san").setUserName("psan"));
        users.add(new UserEntity().setId(3L).setName("mia").setLastName("jarux").setUserName("mjarux"));

        when(userService.getList()).thenReturn(users);
        List<UserEntity> expectedUsers = users;

        assertEquals(expectedUsers, users);
    }

    @Test
    @DisplayName("When repository respond ok, then service return same object")
    void whenGetUserByNameReturnOk() {
        UserEntity userEntity = mockUserEntity().setId(USER_ID).setName(USER_NAME);
        when(userRepository.getByUserName(anyString())).thenReturn(Optional.of(userEntity));

        UserEntity user = userService.getByUserName(USER_NAME);

        assertEquals(userEntity, user);
        verify(userRepository).getByUserName(USER_NAME);
    }

    @Test
    @DisplayName("When get information and repository throw NotFoundException")
    public void getInformationThrowsNotFoundException() {
        when(userRepository.getById(anyLong())).thenThrow(new NotFoundException());

        assertThrows(
                NotFoundException.class,
                () -> userService.getInformation(anyLong())
        );
    }

    @Test
    public void DeleteUser() {
        UserEntity userEntity = mockUserEntity();
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(userEntity));

        userService.delete(USER_ID);
        verify(userRepository).delete(userEntity);
    }

    @Test
    public void updateUser() {
        UserEntity userEntity = mockUserEntity();
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(userEntity));

        userService.update(USER_ID, userEntity);
        verify(userRepository).save(userEntity);
    }

    @Test
    public void saveUser() {
        UserEntity userEntity = mockUserEntity();
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(userEntity));

        userService.save(userEntity);
        verify(userRepository).save(userEntity);
    }

    @Test
    public void getByTelegramId() {
        UserEntity userEntity = mockUserEntity();
        when(userRepository.getByTelegramId(anyLong())).thenReturn(Optional.of(userEntity));

        userService.getByTelegramId(USER_ID);
        verify(userRepository).getByTelegramId(USER_ID);
    }

    @Test
    public void validateTelegramId() {
        UserEntity userEntity = mockUserEntity();
        when(userRepository.getByTelegramId(anyLong())).thenReturn(Optional.of(userEntity));

        userService.validateTelegramId(USER_ID);
        verify(userRepository).getByTelegramId(USER_ID);
    }

    @Test
    @DisplayName("When signout from telegram and repository throw NotFoundException")
    public void signoutFromTelegramThrowsNotFoundException() {
        when(userRepository.getByTelegramId(anyLong())).thenThrow(new NotFoundException());

        assertThrows(
                NotFoundException.class,
                () -> userService.signoutFromTelegram(anyLong())
        );
    }

    @Test
    @DisplayName("When signup from telegram and repository throw NotFoundException")
    public void signupFromTelegramThrowsNotFoundException() {
        TelegramUser telegramUser = new TelegramUser().setUserName(USER_NAME);
        when(userRepository.getByUserName(anyString())).thenThrow(new NotFoundException());

        assertThrows(
                NotFoundException.class,
                () -> userService.signupFromTelegram(telegramUser)
        );
    }

    @Test
    public void getInformation() {
        UserEntity userEntity = mockUserEntity();
        UserInformation userInformation = mockUserInformation();
        List<CountryListEntity> list = new ArrayList<>();

        when(userRepository.getById(anyLong())).thenReturn(Optional.of(userEntity));
        when(countryListRepository.findByUserId(anyLong())).thenReturn(list);

        UserInformation expected = userService.getInformation(USER_ID);

        assertEquals(userInformation.getId(), expected.getId());
    }
}
