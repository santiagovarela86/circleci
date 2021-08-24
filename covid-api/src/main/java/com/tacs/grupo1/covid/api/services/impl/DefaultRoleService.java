package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.RoleRepository;
import com.tacs.grupo1.covid.api.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("roleService")
public class DefaultRoleService implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public DefaultRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleEntity get(long id) {
        return roleRepository.getById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public RoleEntity get(String name) {
        return roleRepository.getByName(name).orElseThrow(NotFoundException::new);
    }

    @Override
    public RoleEntity save(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public RoleEntity update(long id, RoleEntity role) {
        RoleEntity roleEntity = get(id);
        return save(mergeRoleEntity(roleEntity, role));
    }

    @Override
    public List<RoleEntity> getList() {
        return StreamSupport.stream(roleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        RoleEntity roleEntity = get(id);
        roleRepository.delete(roleEntity);
    }

    private RoleEntity mergeRoleEntity(RoleEntity actual, RoleEntity role) {
        return actual.setName(role.getName());
    }
}
