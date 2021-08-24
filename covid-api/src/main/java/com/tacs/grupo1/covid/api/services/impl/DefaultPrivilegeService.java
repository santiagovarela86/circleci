package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.PrivilegeRepository;
import com.tacs.grupo1.covid.api.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("privilegeService")
public class DefaultPrivilegeService implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public DefaultPrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public PrivilegeEntity get(long id) {
        return privilegeRepository.getById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public PrivilegeEntity get(String name) {
        return privilegeRepository.getByName(name).orElseThrow(NotFoundException::new);
    }

    @Override
    public PrivilegeEntity save(PrivilegeEntity privilege) {
        return privilegeRepository.save(privilege);
    }

    @Override
    public PrivilegeEntity update(long id, PrivilegeEntity privilege) {
        PrivilegeEntity privilegeEntity = get(id);
        return save(mergePrivilegeEntity(privilegeEntity, privilege));
    }

    @Override
    public List<PrivilegeEntity> getList() {
        return StreamSupport.stream(privilegeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        PrivilegeEntity privilegeEntity = get(id);
        privilegeRepository.delete(privilegeEntity);
    }

    private PrivilegeEntity mergePrivilegeEntity(PrivilegeEntity actual, PrivilegeEntity privilege) {
        return actual.setName(privilege.getName());
    }
}
