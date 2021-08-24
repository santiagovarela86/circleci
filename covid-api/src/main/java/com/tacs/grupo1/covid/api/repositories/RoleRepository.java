package com.tacs.grupo1.covid.api.repositories;

import com.tacs.grupo1.covid.api.domain.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    public Optional<RoleEntity> getById(long id);

    public Optional<RoleEntity> getByName(String name);

}
