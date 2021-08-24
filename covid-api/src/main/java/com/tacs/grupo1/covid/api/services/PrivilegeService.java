package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;

import java.util.List;

public interface PrivilegeService {

    PrivilegeEntity get(long id);

    PrivilegeEntity get(String name);

    PrivilegeEntity save(PrivilegeEntity role);

    PrivilegeEntity update(long id, PrivilegeEntity role);

    List<PrivilegeEntity> getList();

    void delete(long id);
}
