package com.tacs.grupo1.covid.api.services.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.CountriesPage;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.dto.CountryInformation;
import com.tacs.grupo1.covid.api.dto.QResponseCountries;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryRepository;
import org.assertj.core.internal.bytebuddy.build.HashCodeAndEqualsPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tacs.grupo1.covid.api.utils.CountryListUtils.mockCountryListEntity;
import static com.tacs.grupo1.covid.api.utils.CountryUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultCountryServiceTest extends BaseTest {

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    DefaultCountryService countryService;

    @Test
    @DisplayName("When repository respond ok, then service return same object")
    void whenGetCountryByIdReturnOk() {
        CountryEntity countryEntity = mockCountryEntity().setId(COUNTRY_ID).setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);
        mockCountryRepositoryGetAll(countryRepository, countryEntity);

        CountryEntity country = countryService.get(COUNTRY_ID);

        assertEquals(countryEntity, country);
        verify(countryRepository).getById(COUNTRY_ID);
    }

    @Nested
    @DisplayName("The service throw an exception")
    class ServiceThrowException {
        @Test
        @DisplayName("in getById method when repository throw NotFoundException")
        void inGetByIdWhenRepositoryThrowNotFoundException() {
            mockCountryRepositoryGetByIdThrow(countryRepository, NotFoundException.class);

            assertThrows(
                    NotFoundException.class,
                    () -> countryService.get(COUNTRY_ID)
            );
        }

        @Test
        @DisplayName("in delete method when repository throw NotFoundException")
        void inDeleteWhenRepositoryThrowNotFoundException() {
            mockCountryRepositoryGetByIdThrow(countryRepository, NotFoundException.class);

            assertThrows(
                    NotFoundException.class,
                    () -> countryService.delete(COUNTRY_ID)
            );
        }
    }

    @Test
    @DisplayName("When repository saves a country then return same country")
    public void saveCountryReturnCountry() {
        CountryEntity countryEntity = mockCountryEntity().setId(COUNTRY_ID).setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);
        when(countryRepository.save(any(CountryEntity.class))).thenReturn(countryEntity);
        CountryEntity returnedCountry = countryService.save(new CountryEntity());

        verify(countryRepository, times(1)).save(Mockito.any(CountryEntity.class));
        assertEquals(countryEntity.getId(), returnedCountry.getId());
    }

    @Test
    public void update() {
        CountryEntity countryEntity = mockCountryEntity().setId(COUNTRY_ID).setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);
        mockCountryRepositoryGetAll(countryRepository, countryEntity);
        when(countryRepository.save(any(CountryEntity.class))).thenReturn(countryEntity);
        countryService.update(COUNTRY_ID, countryEntity);
        verify(countryRepository).save(any(CountryEntity.class));
    }

    @Test
    @DisplayName("getAll_Response_SUCCESS")
    public void validateGetAll() {

        List<CountryEntity> list = new ArrayList<CountryEntity>();
        list.add(new CountryEntity().setName("Argentina").setIsoCountryCode("AR"));
        list.add(new CountryEntity().setName("Paraguay").setIsoCountryCode("PY"));
        list.add(new CountryEntity().setName("Uruguay").setIsoCountryCode("UY"));

        when(countryRepository.findAll()).thenReturn(list);

        List<CountryEntity> listRes = countryService.getAll();

        System.out.println("---- " + listRes.size());
        assertEquals(3, listRes.size());
    }

    @Test
    @DisplayName("getByIsoCountryCode_Response_SUCCESS")
    public void validateGetByIsoCountryCode() {

        CountryEntity countryEntity = new CountryEntity().setName("Argentina").setIsoCountryCode("AR");

        when(countryRepository.findByIsoCountryCode(anyString())).thenReturn(Optional.of(countryEntity));

        CountryEntity res = countryService.getByIsoCountryCode("AR");
        System.out.println("---- " + res.getName());
        assertEquals("Argentina", res.getName());
    }

    @Test
    @DisplayName("getOptionalByIsoCountryCode_Response_SUCCESS")
    public void validateGetOptionalByIsoCountryCode() {

        CountryEntity countryEntity = new CountryEntity().setName("Argentina").setIsoCountryCode("AR");

        when(countryRepository.findByIsoCountryCode(anyString())).thenReturn(Optional.of(countryEntity));

        Optional<CountryEntity> res = countryService.getOptionalByIsoCountryCode("AR");
        System.out.println("---- " + res.get().getName());
        assertEquals("Argentina", res.get().getName());
    }

    @Test
    @DisplayName("deleteCountry")
    public void validateDeleteCountry() {

        CountryEntity mockCountryEntity = new CountryEntity().setId(1L).setName("Argentina").setIsoCountryCode("AR");

        when(countryRepository.getById(1L)).thenReturn(Optional.of(mockCountryEntity));

        countryService.delete(1L);
        verify(countryRepository).delete(mockCountryEntity);

    }

    @Test
    @DisplayName("getList_Response_SUCCESS")
    public void validateGetList() {

        List<CountryEntity> list = new ArrayList<CountryEntity>();
        list.add(new CountryEntity().setName("Argentina").setIsoCountryCode("AR"));
        list.add(new CountryEntity().setName("Paraguay").setIsoCountryCode("PY"));
        list.add(new CountryEntity().setName("Uruguay").setIsoCountryCode("UY"));

        when(countryRepository.findAll()).thenReturn(list);

        List<CountryEntity> listRes = countryService.getList();

        System.out.println("---- " + listRes.size());
        assertEquals(3, listRes.size());
    }

    @Test
    @DisplayName("getInformation_Response_SUCCESS")
    public void validateGetInformation() {

        List<QResponseCountries> list = new ArrayList<QResponseCountries>();
        list.add(new QResponseCountries().setCountryName("Argentina").setListName("Lista_1").setUserName("User1"));
        list.add(new QResponseCountries().setCountryName("Argentina").setListName("Lista_10").setUserName("User2"));

        when(countryRepository.getJoinInformation(COUNTRY_ID)).thenReturn(list);

        List<QResponseCountries> listRes = countryService.getInformation(COUNTRY_ID);
        System.out.println("---- " + listRes.size());
        assertEquals(2, listRes.size());
    }

    @Test
    @DisplayName("getCountryInterest_NotFoundException")
    public void validateGetCountryInterest_NotFoundException() {

        when(countryRepository.getById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> {
            countryService.getCountryInterest(anyLong());
                    System.out.println("Exception: NotFoundException");
        });
    }

    @Test
    @DisplayName("getCountryInterest_Response_SUCCESS")
    public void validateGetCountryInterest() {

        List<String> list = new ArrayList<String>();
        list.add("Pepe");
        list.add("Jorge");
        list.add("Luisa");

        CountryEntity countryEntity = new CountryEntity().setId(COUNTRY_ID).setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);

        when(countryRepository.getJoinCountryInterest(COUNTRY_ID)).thenReturn(list);
        when(countryRepository.getById(COUNTRY_ID)).thenReturn(Optional.of(countryEntity));

        CountryInformation res = countryService.getCountryInterest(COUNTRY_ID);

        assertEquals(COUNTRY_NAME, res.getCountryName());
        assertEquals(list.size(), res.getTotalUsers());
        assertIterableEquals(list,res.getUsersNames());
    }


    @Test
    @DisplayName("getListPageByName_Response_SUCCESS")
    public void validateGetListPageByName() {

        List<CountryEntity> listCountryEntity = new ArrayList<CountryEntity>();
        listCountryEntity.add(new CountryEntity().setName("Chile").setIsoCountryCode("CL"));
        listCountryEntity.add(new CountryEntity().setName("China").setIsoCountryCode("CN"));
        listCountryEntity.add(new CountryEntity().setName("Cuba").setIsoCountryCode("CU"));
        int pageNumber=0;
        int pageSize=10;
        String prefix="C";
        Pageable sortedByNameAsc = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        Page<CountryEntity> pagedResponse = new PageImpl(listCountryEntity);

        when(countryRepository.findAllByNameStartingWith(prefix,sortedByNameAsc))
                .thenReturn(pagedResponse);

        CountriesPage res = countryService.getListPageByName(prefix,pageNumber, pageSize);

        System.out.println("--getTotalElements()= " + res.getTotalElements());
        assertEquals(listCountryEntity.size(), res.getTotalElements());

        System.out.println("--getPageNumber()= " + res.getPageNumber());
        assertEquals(pageNumber, res.getPageNumber());

        System.out.println("--res.getCountryList().size()= " + res.getCountryList().size());
        assertEquals(listCountryEntity.size(), res.getCountryList().size());
    }


}
