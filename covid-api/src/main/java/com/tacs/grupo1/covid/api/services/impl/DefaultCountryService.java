package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.*;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryRepository;
import com.tacs.grupo1.covid.api.repositories.CovidRepository;
import com.tacs.grupo1.covid.api.services.CountryService;
import com.tacs.grupo1.covid.api.util.ConvertCountry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service("countryService")
public class DefaultCountryService implements CountryService {

    private final CountryRepository countryRepository;
    private final CovidRepository covidRepository;

    @Autowired
    public DefaultCountryService(CountryRepository countryRepository, CovidRepository covidRepository) {
        this.countryRepository = countryRepository;
        this.covidRepository = covidRepository;
    }

    public CountryEntity get(long id) {
        return countryRepository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Optional<CountryEntity> optionalGetByID(long id) {
        return countryRepository.getById(id);
    }

    public List<CountryEntity> getAll() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public CountryEntity getByIsoCountryCode(String isoCode) {
        return countryRepository.findByIsoCountryCode(isoCode).orElseThrow(NotFoundException::new);
    }

    public Optional<CountryEntity> getOptionalByIsoCountryCode(String isoCode) {
        return countryRepository.findByIsoCountryCode(isoCode);
    }

    @Override
    public CountryEntity save(CountryEntity country) {
        return countryRepository.save(country);
    }

    @Override
    public CountryEntity update(long id, CountryEntity country) {
        CountryEntity countryEntity = get(id);
        return save(mergeCountryEntity(countryEntity, country));
    }

    @Override
    public CountriesPage getListPageByName(String prefix, int pageNumber, int pageSize) {

        Pageable sortedByNameDesc = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        Page<CountryEntity> pageCountryEntity =
                countryRepository.findAllByNameStartingWith(prefix,sortedByNameDesc);

        List<CountryEntity> listCountryEntity = StreamSupport
                .stream(pageCountryEntity.getContent().spliterator(), false).collect(Collectors.toList());

        List<Country> listCountry = listCountryEntity
                .stream().map(ConvertCountry::entityToDto).collect(Collectors.toList());

        CountriesPage countriesPage = new CountriesPage()
                .setCountryList(listCountry)
                .setTotalPages(pageCountryEntity.getTotalPages())
                .setPageNumber(pageNumber)
                .setTotalElements(pageCountryEntity.getTotalElements());

        return countriesPage;
    }

    @Override
    public void initializeDB() {
        List<CountryEntity> countries = covidRepository.getAllCountries();
        countries.parallelStream().forEach(country -> {
            //awful but quick
            country.setStrategy("No Analizado");
            countryRepository.save(country);
            log.info("País: " + country.getName() + " agregado.");
            });
    }

    @Override
    public void purgeDB() {
        List<CountryEntity> emptyCountries = countryRepository.findAllByStartDateNull().orElseThrow(NotFoundException::new);
        emptyCountries.parallelStream().forEach(country -> countryRepository.delete(country));
    }

    @Override
    public List<CountryEntity> getList() {
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        CountryEntity countryEntity = get(id);
        countryRepository.delete(countryEntity);
    }

    @Override
    public List<QResponseCountries> getInformation(long id) {
        return countryRepository.getJoinInformation(id);
    }

    @Override
    public CountryInformation getCountryInterest(long id) {

        Optional<CountryEntity> country = countryRepository.getById(id);
        if (country.isEmpty()) {
            throw new NotFoundException();
        }

        List<String> listUsersNames = countryRepository.getJoinCountryInterest(id);

        return new CountryInformation()
                .setCountryName(country.get().getName())
                .setTotalUsers(listUsersNames.size())
                .setUsersNames(listUsersNames);
    }

    private CountryEntity mergeCountryEntity(CountryEntity actual, CountryEntity country) {
        return actual.setName(country.getName())
                .setIsoCountryCode(country.getIsoCountryCode())
                .setStartDate(country.getStartDate())
                .setOffsetVal(country.getOffsetVal())
                .setStrategy(country.getStrategy());
    }
}
