package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.websecurity.JwtRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface SessionService {

    UserEntity signUp(UserEntity user);
    ResponseEntity<?> login(JwtRequest jwtRequest) throws Exception;
    String hashPassword(String plainPassword);
    BCryptPasswordEncoder getRandomEncoder();
    ResponseEntity<?> health();
    //AuthToken login(LoginCredentials credentials);
    //AuthToken logout(AuthToken token);

}
