package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.RoleEntity;

import java.util.List;

public interface RoleService {

    RoleEntity get(long id);

    RoleEntity get(String name);

    RoleEntity save(RoleEntity role);

    RoleEntity update(long id, RoleEntity role);

    List<RoleEntity> getList();

    void delete(long id);
}
