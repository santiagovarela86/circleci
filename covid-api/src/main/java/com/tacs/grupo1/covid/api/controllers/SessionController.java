package com.tacs.grupo1.covid.api.controllers;

import com.tacs.grupo1.covid.api.dto.User;
import com.tacs.grupo1.covid.api.services.SessionService;
import com.tacs.grupo1.covid.api.util.ConvertUser;
import com.tacs.grupo1.covid.api.websecurity.JwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

    private final SessionService sessionService;
    private final ConvertUser convertUser;

    @Autowired
    public SessionController(SessionService sessionService, ConvertUser convertUser) {
        this.sessionService = sessionService;
        this.convertUser = convertUser;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public User signUp(@Valid @RequestBody User user) {
        return convertUser.entityToDto(this.sessionService.signUp(convertUser.dtoToEntity(user)));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequest authenticationRequest) throws Exception {
        return this.sessionService.login(authenticationRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/health")
    public ResponseEntity<?> health(){
        return this.sessionService.health();
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/isAdmin")
    public ResponseEntity<?> isAdmin(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
