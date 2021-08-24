package com.tacs.grupo1.covid.api.controllers;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.statistics.CountryPlot;
import com.tacs.grupo1.covid.api.dto.statistics.CountryStatistics;
import com.tacs.grupo1.covid.api.services.*;
import com.tacs.grupo1.covid.api.websecurity.JwtTokenProvider;
import com.tacs.grupo1.covid.api.websecurity.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final PlotService plotService;
    private final CountryListService countryListService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final UserService userService;
    private final CountryService countryService;

    @Autowired
    public StatisticsController(CountryService countryService, StatisticsService statisticsService, UserService userService, JwtUserDetailsService jwtUserDetailsService, PlotService plotService, CountryListService countryListService, JwtTokenProvider jwtTokenProvider) {
        this.statisticsService = statisticsService;
        this.plotService = plotService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.countryListService = countryListService;
        this.countryService = countryService;
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/countries/{countryId}/stats")
    public CountryStatistics getCountryStatics(
            @PathVariable long countryId
    ) {
        return statisticsService.getCountryStatistics(countryId);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/country-lists/{countryListId}/stats")
    public ResponseEntity<?> getCountryStaticsForCountryList(
            @PathVariable long countryListId,
            @RequestHeader("Authorization") String bearertoken) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("TELEGRAM")) || countryListService.get(countryListId).getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(statisticsService.getCountryListStatistics(countryListId), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/countries/{countryId}/plot")
    public CountryPlot getCountryPlot(
            @PathVariable long countryId
    ) {
        return plotService.getCountryPlot(countryId);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/country-lists/{countryListId}/plot")
    public ResponseEntity<?> getCountryPlotForCountryList(
            @PathVariable long countryListId,
            @RequestParam(defaultValue = "0") Long d,
            @RequestHeader("Authorization") String bearertoken) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("TELEGRAM")) || countryListService.get(countryListId).getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(plotService.getCountryListPlot(countryListId, d), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/country-lists/plot")
    public ResponseEntity<?> getCountryPlotForOnTheFlyCountryList(
            @RequestBody CountryList countryList,
            @RequestHeader("Authorization") String bearertoken) {
        bearertoken = bearertoken.substring(7, bearertoken.length());

        //country list size between 1 and 4
        if (countryList.getCountries().size() >= 1 && countryList.getCountries().size() <= 4) {
            //all countries do exist
            if (countryList.getCountries().stream().allMatch(country ->
                    countryService.getOptionalByIsoCountryCode(country.getIsoCountryCode()).isPresent())) {
                return new ResponseEntity<>(plotService.getCountryListPlotOnTheFly(countryList), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Debe elegir países con código ISO válido", HttpStatus.BAD_REQUEST);
            }
        } else return new ResponseEntity<>("Debe elegir entre 1 y 4 listas para plotear", HttpStatus.BAD_REQUEST);
    }
}
