package com.tacs.grupo1.covid.api.utils;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CountryListUtils extends BaseTest {

    public static CountryList mockCountryList() {
        return new CountryList().setId(COUNTRY_LIST_ID).setName(COUNTRY_LIST_NAME);
    }

    public static CountryListEntity mockCountryListEntity() {
        return new CountryListEntity().setId(COUNTRY_LIST_ID).setName(COUNTRY_LIST_NAME).setCountries(Set.of(CountryUtils.mockCountryEntity()));
    }

    public static void mockCountryListRepositoryGetAll(CountryListRepository countryListRepository) {
        mockCountryListRepositoryGetAll(countryListRepository, mockCountryListEntity());
    }

    public static void mockCountryListRepositoryGetAll(CountryListRepository countryListRepository, CountryListEntity countryListEntity) {
        when(countryListRepository.getById(anyLong())).thenReturn(Optional.of(countryListEntity));
    }

    public static void mockCountryListRepositoryGetByIdThrow(CountryListRepository countryListRepository) {
        mockCountryListRepositoryGetByIdThrow(countryListRepository, NotFoundException.class);
    }

    public static void mockCountryListRepositoryGetByIdThrow(CountryListRepository countryListRepository, Class<? extends Throwable> throwableType) {
        when(countryListRepository.getById(anyLong())).thenThrow(throwableType);
    }
}
