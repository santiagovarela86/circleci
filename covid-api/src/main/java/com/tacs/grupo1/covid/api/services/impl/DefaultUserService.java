package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.TelegramUser;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import com.tacs.grupo1.covid.api.services.SessionService;
import com.tacs.grupo1.covid.api.services.UserService;
import com.tacs.grupo1.covid.api.util.ConvertCountryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("userService")
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final CountryListRepository countryListRepository;
    private final SessionService sessionService;

    @Autowired
    public DefaultUserService(UserRepository userRepository, CountryListRepository countryListRepository, SessionService sessionService) {
        this.userRepository = userRepository;
        this.countryListRepository = countryListRepository;
        this.sessionService = sessionService;
    }


    public UserEntity get(long id) {
        return userRepository.getById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public UserEntity getByUserName(String userName) {
        return userRepository.getByUserName(userName).orElseThrow(NotFoundException::new);
    }

    @Override
    public UserEntity getByTelegramId(long telegramId) {
        return userRepository.getByTelegramId(telegramId).orElseThrow(NotFoundException::new);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity update(long id, UserEntity user) {
        UserEntity userEntity = get(id);
        return save(mergeUserEntity(userEntity, user));
    }

    @Override
    public List<UserEntity> getList() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        UserEntity userEntity = get(id);
        userRepository.delete(userEntity);
    }

    @Override
    public UserInformation getInformation(long id) {
        Optional<UserEntity> user = userRepository.getById(id);

        if (user.isEmpty()) {
            throw new NotFoundException();
        }

        List<CountryList> countryLists = countryListRepository.findByUserId(id).stream().map(ConvertCountryList::entityToDto).collect(Collectors.toList());
        long totalLists = countryLists.size();

        long totalCountries = 0;
        for (CountryList e : countryLists) {
            totalCountries += e.getCountries().size();
        }

        return new UserInformation()
                .setId(user.get().getId())
                .setName(user.get().getName())
                .setLastName(user.get().getLastName())
                .setUserName(user.get().getUserName())
                .setTelegramId(user.get().getTelegramId())
                .setEmail(user.get().getEmail())
                .setLastLogin(user.get().getLastLogin())
                .setTotalCountries(totalCountries)
                .setTotalLists(totalLists);
    }

    @Override
    public UserInformation validateTelegramId(long id) {
        // Busca el usuario por id de telegram
        Optional<UserEntity> user = userRepository.getByTelegramId(id);

        if (user.isEmpty()) {
            return new UserInformation();
        }
        return new UserInformation()
                .setId(user.get().getId())
                .setUserName(user.get().getUserName())
                .setTelegramId(user.get().getTelegramId())
                .setName(user.get().getName())
                .setLastName(user.get().getLastName())
                .setEmail(user.get().getEmail());
    }

    @Override
    public UserInformation signupFromTelegram(TelegramUser telegramUser) {
        //CASE SENSITIVE USER...
        Optional<UserEntity> user = userRepository.getByUserName(telegramUser.getUserName());

        //if the user doesn't exist
        if (user.isEmpty()) {
            return new UserInformation().setUserName("USER_DOESNT_EXIST");
        } else {
            //if the credentials are correct
            if (sessionService.getRandomEncoder().matches(telegramUser.getPassword(), user.get().getPassword())) {

                update(user.get().getId(), user.get().setTelegramId(telegramUser.getTelegramId()));

                return new UserInformation()
                        .setId(user.get().getId())
                        .setUserName(user.get().getUserName())
                        .setTelegramId(user.get().getTelegramId())
                        .setName(user.get().getName())
                        .setLastName(user.get().getLastName())
                        .setEmail(user.get().getEmail());
            } else {
                return new UserInformation().setUserName("BAD_CREDENTIALS");
            }
        }
    }

    @Override
    public UserInformation signoutFromTelegram(long telegramId) {
        //CASE SENSITIVE USER...
        Optional<UserEntity> user = userRepository.getByTelegramId(telegramId);

        //if the user doesn't exist
        if (user.isEmpty()) {
            return new UserInformation().setUserName("USER_ALREADY_DEREGISTERED");
        } else {

            if (!user.get().getTelegramId().equals(telegramId)) {
                return new UserInformation().setUserName("WRONG_TELEGRAM_ID");
            } else {
                userRepository.save(user.get().setTelegramId(null));
                return new UserInformation().setUserName("SUCCESS");
            }
        }
    }

    private UserEntity mergeUserEntity(UserEntity actual, UserEntity user) {
        return actual.setName(user.getName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setUserName(user.getUserName())
                .setRoles(user.getRoles())
                .setLastLogin(user.getLastLogin())
                .setCountryLists(user.getCountryLists())
                .setTelegramId(user.getTelegramId());
    }
}
