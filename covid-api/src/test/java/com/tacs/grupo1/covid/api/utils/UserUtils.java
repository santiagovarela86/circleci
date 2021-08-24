package com.tacs.grupo1.covid.api.utils;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.User;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class UserUtils extends BaseTest {

    public static User mockUser() {
        return new User().setId(USER_ID).setName(USER_NAME);
    }

    public static UserEntity mockUserEntity() {
        return new UserEntity().setId(USER_ID).setName(USER_NAME);
    }

    public static UserInformation mockUserInformation() {
        UserEntity user = mockUserEntity();

        return new UserInformation()
                .setId(user.getId())
                .setUserName(user.getUserName())
                .setTelegramId(user.getTelegramId())
                .setName(user.getName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail());
    }

    public static void mockUserRepositoryGetAll(UserRepository userRepository) {
        mockUserRepositoryGetAll(userRepository, mockUserEntity());
    }

    public static void mockUserRepositoryGetAll(UserRepository userRepository, UserEntity userEntity) {
        when(userRepository.getById(anyLong())).thenReturn(Optional.of(userEntity));
    }

    public static void mockUserRepositoryGetByIdThrow(UserRepository userRepository) {
        mockUserRepositoryGetByIdThrow(userRepository, NotFoundException.class);
    }

    public static void mockUserRepositoryGetByIdThrow(UserRepository userRepository, Class<? extends Throwable> throwableType) {
        when(userRepository.getById(anyLong())).thenThrow(throwableType);
    }
}
