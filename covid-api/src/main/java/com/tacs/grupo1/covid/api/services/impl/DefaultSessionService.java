package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.exceptions.SignUpException;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import com.tacs.grupo1.covid.api.services.RoleService;
import com.tacs.grupo1.covid.api.services.SessionService;
import com.tacs.grupo1.covid.api.websecurity.JwtRequest;
import com.tacs.grupo1.covid.api.websecurity.JwtResponse;
import com.tacs.grupo1.covid.api.websecurity.JwtTokenProvider;
import com.tacs.grupo1.covid.api.websecurity.JwtUserDetailsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service("sessionService")
public class DefaultSessionService implements SessionService {

    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private JwtUserDetailsService jwtUserDetailsService;
    private RoleService roleService;
    private static BCryptPasswordEncoder randomEncoder;

    @Autowired
    public DefaultSessionService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, JwtUserDetailsService jwtUserDetailsService, RoleService roleService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.roleService = roleService;
    }

    @Override
    public UserEntity signUp(UserEntity user) {
        if (isValidForSignUp(user)){
            if (userEmailDoesntExist(user)) {
                if (userNameDoesntExist(user)){
                    user.setPassword(hashPassword(user.getPassword()));
                    //ROLE ASSIGNMENT
                    RoleEntity userRole = roleService.get("ROLE_USER");
                    user.setRoles(Arrays.asList(userRole));
                    //HORRIBLE
                    userRole.getUsers().add(user);
                    roleService.save(userRole);
                    return userRepository.save(user);
                }else{
                    throw new SignUpException("Ese nombre de usuario ya se ha utilizado, elija otro.");
                }
            }else{
                throw new SignUpException("Ese correo electrónico ya está registrado en el sistema, elija otro.");
            }
        }else{
            throw new SignUpException("Falta información para dar de alta al usuario. Complete todos los datos necesarios.");
        }
    }

    private Boolean isValidForSignUp(UserEntity user){
        return (user.getEmail() != null &&
                user.getLastName() != null &&
                user.getName() != null &&
                user.getPassword() != null &&
                user.getUserName() != null);
    }

    private Boolean userEmailDoesntExist(UserEntity user){
        Optional<UserEntity> existingUser = userRepository.getByEmail(user.getEmail());
        return existingUser.isEmpty();
    }

    private Boolean userNameDoesntExist(UserEntity user){
        Optional<UserEntity> existingUser = userRepository.getByUserName(user.getUserName());
        return existingUser.isEmpty();
    }

    @Override
    public ResponseEntity<?> login(JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenProvider.generateToken(userDetails);
        var user = this.userRepository.getByUserName(authenticationRequest.getUsername());
        user.get().setLastLogin(LocalDateTime.now());
        this.userRepository.save(user.get());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public BCryptPasswordEncoder getRandomEncoder(){
        if (this.randomEncoder == null){
            this.randomEncoder = new BCryptPasswordEncoder();
        }
        return this.randomEncoder;
    }

    @Override
    public ResponseEntity<?> health() {
        JSONObject body = new JSONObject();
        body.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        return ResponseEntity.ok(body.toString());
    }

    public String hashPassword(String plainPassword) {
        return this.getRandomEncoder().encode(plainPassword);
    }
}
