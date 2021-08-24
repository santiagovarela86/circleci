package com.tacs.grupo1.covid.api.controllers;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.CountriesPage;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.dto.CountryInformation;
import com.tacs.grupo1.covid.api.dto.QResponseCountries;
import com.tacs.grupo1.covid.api.services.CountryService;
import com.tacs.grupo1.covid.api.util.ConvertCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tacs.grupo1.covid.api.util.ConvertCountry.entityToDto;

@RestController
@CrossOrigin
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/countries")
    public List<Country> getAll() {
        return countryService.getList().stream().map(ConvertCountry::entityToDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('TELEGRAM')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/countries-page")
    public CountriesPage getCountriesPage(
            @RequestParam String prefix,
            @RequestParam int pageNum
    ){
        return countryService.getListPageByName(prefix,pageNum,10);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/countries/{id}")
    public Country get(
            @PathVariable long id) {
        return entityToDto(countryService.get(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/countries/{id}")
    public ResponseEntity<?> update(
            @PathVariable long id,
            @Valid @RequestBody Country newCountry) {

        Optional<CountryEntity> country = countryService.optionalGetByID(id);

        if (country.isPresent()){
            if (country.get().getId().equals(newCountry.getId())){
                List<String> possibleStrategies = new ArrayList<String>();
                //awful but quick
                possibleStrategies.add("No Analizado");
                possibleStrategies.add("Cuarentena");
                possibleStrategies.add("Libre Circulación");
                possibleStrategies.add("Distanciamiento Social");

                if (possibleStrategies.stream().anyMatch(str -> str.equals(newCountry.getStrategy()))){
                    country.get().setStrategy(newCountry.getStrategy());
                    countryService.update(id, country.get());
                    return new ResponseEntity<Country>(entityToDto(country.get()), HttpStatus.OK);
                }else{
                    return new ResponseEntity<String>("Wrong strategy", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<String>("Wrong country", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/country-information-test/{id}")
    public List<QResponseCountries> getInformation(
            @PathVariable long id
    ) {
        return countryService.getInformation(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/country-interest/{id}")
    public CountryInformation getCountryInterest(
            @PathVariable long id
    ) {
        return countryService.getCountryInterest(id);
    }
}
