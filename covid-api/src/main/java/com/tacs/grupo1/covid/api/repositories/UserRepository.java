package com.tacs.grupo1.covid.api.repositories;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public Optional<UserEntity> getById(long id);

    public Optional<UserEntity> getByEmail(String email);

    public Optional<UserEntity> getByUserName(String userName);

    public  Optional<UserEntity> getByTelegramId(long telegramId);

    public  Optional<UserEntity> findByUserNameAndPassword(String userName, String password);

    //public Optional<UserEntity> getByRoles(List<String> roleNames);

    // todo: query para obtener todos los paises que utiliza el user en sus listas sin repetir
    //public long getTotalCountries(long id);

    // todo: query para obtener la cantidad de listas de un user
    //public long getTotalLists(long id);
}

