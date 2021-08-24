package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.CountriesPage;
import com.tacs.grupo1.covid.api.dto.CountryInformation;
import com.tacs.grupo1.covid.api.dto.QResponseCountries;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    CountryEntity get(long id);

    Optional<CountryEntity> optionalGetByID(long id);

    CountryEntity save(CountryEntity country);

    CountryEntity update(long id, CountryEntity country);

    List<CountryEntity> getList();

    void delete(long id);

    List<QResponseCountries> getInformation(long id);

    CountryInformation getCountryInterest(long id);

    //  En desarrollo..
    CountriesPage getListPageByName(String prefix, int pageNumber , int pageSize);

    public CountryEntity getByIsoCountryCode(String isoCountryCode);

    public Optional<CountryEntity> getOptionalByIsoCountryCode(String isoCountryCode);

    public List<CountryEntity> getAll();

    void initializeDB();

    void purgeDB();
}
