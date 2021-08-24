package com.tacs.grupo1.covid.api.repositories;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

public interface PrivilegeRepository extends CrudRepository<PrivilegeEntity, Long> {

    public Optional<PrivilegeEntity> getById(long id);

    public Optional<PrivilegeEntity> getByName(String name);

}
