package com.tacs.grupo1.covid.api.controllers;

import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.CountryListsStats;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.CountryService;
import com.tacs.grupo1.covid.api.services.UserService;
import com.tacs.grupo1.covid.api.util.ConvertCountry;
import com.tacs.grupo1.covid.api.util.ConvertCountryList;
import com.tacs.grupo1.covid.api.websecurity.JwtTokenProvider;
import com.tacs.grupo1.covid.api.websecurity.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tacs.grupo1.covid.api.util.ConvertCountryList.dtoToEntity;
import static com.tacs.grupo1.covid.api.util.ConvertCountryList.entityToDto;

@RestController
@CrossOrigin
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryListController {

    private final CountryListService countryListService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final UserService userService;
    private final CountryService countryService;

    @Autowired
    public CountryListController(CountryListService countryListService, CountryService countryService, JwtTokenProvider jwtTokenProvider, JwtUserDetailsService jwtUserDetailsService, UserService userService) {
        this.countryListService = countryListService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.countryService = countryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/country-lists")
    public List<CountryList> getAll(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return countryListService.getList(pageNumber, pageSize, sortBy).stream().map(ConvertCountryList::entityToDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/country-lists/{id}")
    public ResponseEntity<?> get(
            @PathVariable long id,
            @RequestHeader("Authorization") String bearertoken) {

        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")) || authorities.stream().anyMatch(auth -> auth.getAuthority().equals("TELEGRAM")) ||countryListService.get(id).getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(entityToDto(countryListService.get(id)), HttpStatus.OK);
        }else{
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/country-lists")
    public ResponseEntity<?> create(
            @Valid @RequestBody CountryList countryList,
            @RequestHeader("Authorization") String bearertoken
    ) {
        Date dateCreation=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            dateCreation = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        System.out.println();
        String listNameInTable =countryListService.findCountryListNameByUserIdAndListName(user.getId(),countryList.getName(),0);
        System.out.println("-userId[" + user.getId() + "] -listId[0] -listName[" + countryList.getName() + "] = "+ listNameInTable);
        if(listNameInTable==null){

            Set<Country> emptyCountries = countryList.getCountries();
            Set<Country> actualCountries = new HashSet<>();

            if (emptyCountries.stream().anyMatch(country -> countryService.getOptionalByIsoCountryCode(country.getIsoCountryCode()).isEmpty())){
                return new ResponseEntity<String>("Verifique la lista de países, algunos de ellos tienen mal su código ISO.", HttpStatus.BAD_REQUEST);
            }else{
                countryList.setUserId(user.getId());
                emptyCountries.stream().forEach(emptyCountry -> {
                    actualCountries.add(new ConvertCountry().entityToDto(countryService.getByIsoCountryCode(emptyCountry.getIsoCountryCode())));
                });
                countryList.setCountries(actualCountries);
            }

            return new ResponseEntity<>(entityToDto(countryListService.save(dtoToEntity(countryList).setCreationDate(dateCreation))), HttpStatus.OK);
        }
        return new ResponseEntity<>("Country List with same name already exists", HttpStatus.CONFLICT);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/country-lists/{id}")
    public ResponseEntity<?> update(
            @PathVariable long id,
            @Valid @RequestBody CountryList countryList,
            @RequestHeader("Authorization") String bearertoken
    ) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("TELEGRAM")) || countryListService.get(id).getUser().getId().equals(user.getId())) {

            String listNameInTable = countryListService.findCountryListNameByUserIdAndListName(user.getId(),countryList.getName(),id);
            System.out.println("-userId[" + user.getId() + "] -listId[" + id +"] -listName[" + countryList.getName() + "] = "+ listNameInTable);
            if(listNameInTable==null) {
                Set<Country> actualCountries = new HashSet<>();

                if (countryList.getCountries().stream().anyMatch(country -> countryService.getOptionalByIsoCountryCode(country.getIsoCountryCode()).isEmpty())) {
                    return new ResponseEntity<String>("Verifique la lista de países, algunos de ellos tienen mal su código ISO.", HttpStatus.BAD_REQUEST);
                } else {
                    countryList.getCountries().stream().forEach(emptyCountry -> {
                        actualCountries.add(new ConvertCountry().entityToDto(countryService.getByIsoCountryCode(emptyCountry.getIsoCountryCode())));
                    });
                    countryList.setId(id);
                    countryList.setCountries(actualCountries);
                    countryList.setCreationDate(new SimpleDateFormat("MM/dd/yyyy").format(countryListService.get(id).getCreationDate()));
                    countryList.setUserId(countryListService.get(id).getUser().getId());
                    countryListService.update(id, dtoToEntity(countryList));
                    return new ResponseEntity<>(entityToDto(countryListService.get(id)), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Country List with same name already exists", HttpStatus.CONFLICT);
        } else{
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/country-lists/{id}")
    public ResponseEntity<?> delete(
            @PathVariable long id,
            @RequestHeader("Authorization") String bearertoken
    ) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();

        if (countryListService.get(id).getUser().getId().equals(user.getId())) {
            countryListService.delete(id);
            return new ResponseEntity<>(ResponseEntity.noContent().build(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/country-lists/{listId}/countries/{countryId}")
    public ResponseEntity<?> update(
            @PathVariable long listId,
            @PathVariable long countryId,
            @RequestHeader("Authorization") String bearertoken
    ) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("TELEGRAM")) || countryListService.get(listId).getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(entityToDto(countryListService.addCountry(listId,countryId)), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list-compare")
    public ResponseEntity<?> get(
            @RequestParam long id1,
            @RequestParam long id2,
            @RequestHeader("Authorization") String bearertoken) {
        bearertoken = bearertoken.substring(7, bearertoken.length());
        String userName = this.jwtTokenProvider.getUsernameFromToken(bearertoken);
        UserEntity user = this.userService.getByUserName(userName);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
        var authorities = userDetails.getAuthorities();

        if (countryListService.getOptional(id1).isPresent() && countryListService.getOptional(id2).isPresent())
            return new ResponseEntity<>(countryListService.compare(id1, id2).stream().map(c -> ConvertCountry.entityToDto(c)).collect(Collectors.toList()), HttpStatus.OK);
        else {
            return new ResponseEntity<String>("Alguna de las listas no existe, no se pueden comparar.", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list-info")
    public CountryListsStats getInfo(
            @RequestParam (defaultValue = "-1")long d) {
        return countryListService.getInfo(d);

    }
}
