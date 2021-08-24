package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.TelegramUser;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class DefaultUserTelegramServiceTest extends BaseTest {


    @Mock
    UserRepository userRepository;

    @Mock
    CountryListRepository countryListRepository;

    @Mock
    DefaultSessionService sessionService;

    @Autowired
    @InjectMocks
    DefaultUserService userService;

    @Test
    @DisplayName("signupFromTelegram_Response_USER_DOESNT_EXIST")
    public void signupFromTelegram_Response_USER_DOESNT_EXIST() {

        TelegramUser telegramUser = new TelegramUser()
                .setUserName("Administrator").setPassword("123456").setTelegramId(10001L);

        when(userRepository.getByUserName(anyString())).thenReturn(Optional.empty());

        UserInformation res =  userService.signupFromTelegram(telegramUser);
        System.out.println("--- " + res.getUserName());
        assertEquals("USER_DOESNT_EXIST",res.getUserName());

    }

    @Test
    @DisplayName("signupFromTelegram_Response_BAD_CREDENTIALS")
    public void signupFromTelegram_Response_BAD_CREDENTIALS() {

        TelegramUser telegramUser = new TelegramUser()
                .setUserName("Administrator").setPassword("1234562").setTelegramId(10001L);

        UserEntity ue = new UserEntity()
                .setId(1L).setUserName("Administrator").setTelegramId(10001L)
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS");


        when(userRepository.getByUserName(anyString())).thenReturn(Optional.of(ue));
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(ue));

        when(userRepository.save(any(UserEntity.class))).thenReturn(ue);

        when(sessionService.getRandomEncoder()).thenCallRealMethod();
        when(sessionService.getRandomEncoder().matches("1234562","$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS")).thenCallRealMethod();

        when(userService.update(1L,ue)).thenReturn(ue);

        UserInformation res =  userService.signupFromTelegram(telegramUser);
        System.out.println("--- " + res.getUserName());
        assertEquals("BAD_CREDENTIALS",res.getUserName());
    }

    @Test
    @DisplayName("signupFromTelegram_Response_OK")
    public void signupFromTelegram_Response_OK() {

        TelegramUser telegramUser = new TelegramUser()
                .setUserName("Administrator").setPassword("123456").setTelegramId(10001L);

        UserEntity ue = new UserEntity()
                .setId(1L).setUserName("Administrator").setTelegramId(10001L)
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS");


        when(userRepository.getByUserName(anyString())).thenReturn(Optional.of(ue));
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(ue));

        when(userRepository.save(any(UserEntity.class))).thenReturn(ue);

        when(sessionService.getRandomEncoder()).thenCallRealMethod();
        when(sessionService.getRandomEncoder().matches("123456","$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS")).thenCallRealMethod();

        when(userService.update(1L,ue)).thenReturn(ue);

        UserInformation res =  userService.signupFromTelegram(telegramUser);
        System.out.println("--- " + res.getUserName());
        assertEquals(telegramUser.getUserName(),res.getUserName());
    }

    @Test
    @DisplayName("signoutFromTelegram_Response_USER_ALREADY_DEREGISTERED")
    public void signoutFromTelegram_Response_USER_ALREADY_DEREGISTERED() {

        TelegramUser telegramUser = new TelegramUser()
                .setUserName("Administrator").setPassword("123456").setTelegramId(10001L);

        when(userRepository.getByUserName(anyString())).thenReturn(Optional.empty());

        UserInformation res =  userService.signoutFromTelegram(telegramUser.getTelegramId());
        System.out.println("--- " + res.getUserName());
        assertEquals("USER_ALREADY_DEREGISTERED",res.getUserName());

    }

    @Test
    @DisplayName("signoutFromTelegram_Response_WRONG_TELEGRAM_ID")
    public void signoutFromTelegram_Response_WRONG_TELEGRAM_ID() {

        TelegramUser telegramUser = new TelegramUser()
            .setUserName("UserTest").setPassword("123456").setTelegramId(10001L);

        UserEntity ue = new UserEntity()
            .setId(1L).setUserName("UserTest").setTelegramId(10002L)
            .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS");

        UserEntity ue2 = new UserEntity()
                .setId(1L).setUserName("UserTest").setTelegramId(null)
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS");

        when(userRepository.getByTelegramId(anyLong())).thenReturn(Optional.of(ue));
        when(userRepository.save(any(UserEntity.class))).thenReturn(ue2);

        UserInformation res =  userService.signoutFromTelegram(telegramUser.getTelegramId());
        System.out.println("--- " + res.getUserName());
        assertEquals("WRONG_TELEGRAM_ID",res.getUserName());
    }

    @Test
    @DisplayName("signoutFromTelegram_Response_SUCCESS")
    public void signoutFromTelegram_Response_SUCCESS() {

        TelegramUser telegramUser = new TelegramUser()
                .setUserName("UserTest").setPassword("123456").setTelegramId(10001L);

        UserEntity ue = new UserEntity()
                .setId(1L).setUserName("UserTest").setTelegramId(10001L)
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS");

        UserEntity ue2 = new UserEntity()
                .setId(1L).setUserName("UserTest").setTelegramId(null)
                .setPassword("$2a$10$oM.HEth8RTultbXULzUdDulh.6Un6N/0CM3EW/gFqTPhhE.znyDXS");

        when(userRepository.getByTelegramId(anyLong())).thenReturn(Optional.of(ue));
        when(userRepository.save(any(UserEntity.class))).thenReturn(ue2);

        UserInformation res =  userService.signoutFromTelegram(telegramUser.getTelegramId());
        System.out.println("--- " + res.getUserName());
        assertEquals("SUCCESS",res.getUserName());

    }


}
