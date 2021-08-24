package com.tacs.grupo1.covid.api.utils;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CountryUtils extends BaseTest {

    public static Country mockCountry() {
        return new Country().setId(COUNTRY_ID).setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);
    }

    public static CountryEntity mockCountryEntity() {
        return new CountryEntity().setId(COUNTRY_ID).setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);
    }

    public static void mockCountryRepositoryGetAll(CountryRepository countryRepository) {
        mockCountryRepositoryGetAll(countryRepository, mockCountryEntity());
    }

    public static void mockCountryRepositoryGetAll(CountryRepository countryRepository, CountryEntity countryEntity) {
        when(countryRepository.getById(anyLong())).thenReturn(Optional.of(countryEntity));
    }

    public static void mockCountryRepositoryGetByIdThrow(CountryRepository countryRepository) {
        mockCountryRepositoryGetByIdThrow(countryRepository, NotFoundException.class);
    }

    public static void mockCountryRepositoryGetByIdThrow(CountryRepository countryRepository, Class<? extends Throwable> throwableType) {
        when(countryRepository.getById(anyLong())).thenThrow(throwableType);
    }
}
