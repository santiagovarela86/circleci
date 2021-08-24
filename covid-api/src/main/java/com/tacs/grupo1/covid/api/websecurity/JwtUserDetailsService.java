package com.tacs.grupo1.covid.api.websecurity;

import com.tacs.grupo1.covid.api.domain.PrivilegeEntity;
import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.repositories.RoleRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service("userDetailsService")
@Transactional
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.getByUserName(userName);

        if (userEntity.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    userEntity.get().getUserName(),
                    userEntity.get().getPassword(),
                    getAuthorities(userEntity.get().getRoles()));
        } else {
            return new org.springframework.security.core.userdetails.User(
                    " ",
                    " ",
                    getAuthorities(Arrays.asList(roleRepository.getByName("ROLE_USER").get())));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {

        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (PrivilegeEntity item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
