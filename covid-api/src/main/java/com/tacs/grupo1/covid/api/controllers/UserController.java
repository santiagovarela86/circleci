package com.tacs.grupo1.covid.api.controllers;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.TelegramUser;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.UserService;
import com.tacs.grupo1.covid.api.util.ConvertCountryList;
import com.tacs.grupo1.covid.api.util.ConvertUser;
import com.tacs.grupo1.covid.api.websecurity.JwtTokenProvider;
import com.tacs.grupo1.covid.api.websecurity.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final CountryListService countryListService;
    private final ConvertUser convertUser;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    public UserController(UserService userService, CountryListService countryListService, ConvertUser convertUser, JwtTokenProvider jwtTokenProvider, JwtUserDetailsService jwtUserDetailsService) {
        this.userService = userService;
        this.countryListService = countryListService;
        this.convertUser = convertUser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String bearertoken) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        return new ResponseEntity<>(convertUser.entityToDto(userService.get(user.getId())), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getList().stream().map(userEntity -> convertUser.entityToDto(userEntity)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/country-lists")
    public ResponseEntity<?> getLists(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestHeader("Authorization") String bearertoken,
            @RequestHeader("Authorization2") Optional<String> telegramtoken
    ) {
        UserEntity user;
        if(telegramtoken.isEmpty()){
            bearertoken = bearertoken.substring(7, bearertoken.length());
            String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
            user = this.userService.getByUserName(userName);
        }
        else{
            String telegramid = telegramtoken.get().substring(9, telegramtoken.get().length());
            user = this.userService.getByTelegramId(Long.parseLong(telegramid));
        }

        return new ResponseEntity<>(countryListService.getListByUser(user.getId(), pageNumber, pageSize, sortBy)
                    .stream().map(ConvertCountryList::entityToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-information/{id}")
    public UserInformation getInformation(
            @PathVariable long id
    ) {
        return userService.getInformation(id);
    }

    @PreAuthorize("hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-telegram")
    public UserInformation getValidateTelegramId(
            @RequestHeader("Authorization2") String telegramtoken
    ) {
        String telgramid = telegramtoken.substring(9, telegramtoken.length());
        return userService.validateTelegramId(Long.parseLong(telgramid));
    }

    @PreAuthorize("hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user-telegram")
    public UserInformation signupTelegramId(
            @Valid @RequestBody TelegramUser user
    ) throws Exception {
        return userService.signupFromTelegram(user);
    }

    @PreAuthorize("hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user-telegram/{telegramId}")
    public UserInformation signoutTelegramId(
            @PathVariable long telegramId
    ) {
        return userService.signoutFromTelegram(telegramId);
    }
}