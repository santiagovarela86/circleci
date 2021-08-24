package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.TelegramUser;
import com.tacs.grupo1.covid.api.dto.UserInformation;

import java.util.List;

public interface UserService {

    UserEntity get(long id);

    UserEntity getByUserName(String userName);

    UserEntity getByTelegramId(long telegramId);

    UserEntity save(UserEntity user);

    UserEntity update(long id, UserEntity user);

    List<UserEntity> getList();

    void delete(long id);

    UserInformation getInformation(long id);

    UserInformation validateTelegramId(long id);

    UserInformation signupFromTelegram(TelegramUser user);

    UserInformation signoutFromTelegram(long telegramId);
}
