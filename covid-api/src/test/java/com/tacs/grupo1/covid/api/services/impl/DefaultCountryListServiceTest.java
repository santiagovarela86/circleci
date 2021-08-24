package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.CountryListsStats;
import com.tacs.grupo1.covid.api.dto.UserInformation;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.services.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.tacs.grupo1.covid.api.utils.CountryListUtils.*;
import static com.tacs.grupo1.covid.api.utils.CountryUtils.mockCountryEntity;
import static com.tacs.grupo1.covid.api.utils.UserUtils.mockUserEntity;
import static com.tacs.grupo1.covid.api.utils.UserUtils.mockUserInformation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultCountryListServiceTest extends BaseTest {

    @Mock
    CountryListRepository countryListRepository;

    @Mock
    DefaultCountryService countryService;

    @Autowired
    @InjectMocks
    DefaultCountryListService countryListService;

    @Test
    @DisplayName("When repository respond ok, then service return same object")
    void whenGetCountryListByIdReturnOk() {
        CountryListEntity countryListEntity = mockCountryListEntity().setId(COUNTRY_LIST_ID).setName(COUNTRY_LIST_NAME);
        mockCountryListRepositoryGetAll(countryListRepository, countryListEntity);

        CountryListEntity countryList = countryListService.get(COUNTRY_LIST_ID);

        assertEquals(countryListEntity, countryList);
        verify(countryListRepository).getById(COUNTRY_LIST_ID);
    }

    @Nested
    @DisplayName("The service throw an exception")
    class ServiceThrowException {
        @Test
        @DisplayName("in getById method when repository throw NotFoundException")
        void inGetByIdWhenRepositoryThrowNotFoundException() {
            mockCountryListRepositoryGetByIdThrow(countryListRepository, NotFoundException.class);

            assertThrows(
                    NotFoundException.class,
                    () -> countryListService.get(COUNTRY_LIST_ID)
            );
        }

        @Test
        @DisplayName("in delete method when repository throw NotFoundException")
        void inDeleteWhenRepositoryThrowNotFoundException() {
            mockCountryListRepositoryGetByIdThrow(countryListRepository, NotFoundException.class);

            assertThrows(
                    NotFoundException.class,
                    () -> countryListService.delete(COUNTRY_ID)
            );
        }
    }

    @Test
    @DisplayName("When repository saves a country list then return same country list")
    public void saveCountryListReturnCountryList() {
        CountryListEntity countryListEntity = mockCountryListEntity().setId(COUNTRY_LIST_ID).setName(COUNTRY_LIST_NAME);
        when(countryListRepository.save(any(CountryListEntity.class))).thenReturn(countryListEntity);
        CountryListEntity returnedCountryList = countryListRepository.save(new CountryListEntity());

        verify(countryListRepository, times(1)).save(Mockito.any(CountryListEntity.class));
        assertEquals(countryListEntity.getId(), returnedCountryList.getId());
    }

    @Test
    public void saveCountryList() {
        CountryListEntity countryListEntity = mockCountryListEntity();
        when(countryListRepository.getById(anyLong())).thenReturn(Optional.of(countryListEntity));

        countryListService.save(countryListEntity);
        verify(countryListRepository).save(countryListEntity);
    }

    @Test
    public void updateCountryList() {
        CountryListEntity countryListEntity = mockCountryListEntity();
        when(countryListRepository.getById(anyLong())).thenReturn(Optional.of(countryListEntity));

        countryListService.update(COUNTRY_LIST_ID, countryListEntity);
        verify(countryListRepository).save(countryListEntity);
    }

    @Test
    public void getOptionalCountryList() {
        CountryListEntity countryListEntity = mockCountryListEntity();
        when(countryListRepository.getById(anyLong())).thenReturn(Optional.of(countryListEntity));

        countryListService.getOptional(COUNTRY_LIST_ID);
        verify(countryListRepository).getById(COUNTRY_LIST_ID);
    }

    @Test
    public void getListByUserShouldReturnExpectedList() {
        Page<CountryListEntity> page = Mockito.mock(Page.class);
        when(page.hasContent()).thenReturn(false);

        when(countryListRepository.findByUserId(anyLong(), any(Pageable.class))).thenReturn(page);
        List<CountryListEntity> list = countryListService.getListByUser(USER_ID, 1, 20, "asd");

        assertEquals(list, new ArrayList<>());
    }

    @Test
    public void getListShouldReturnExpectedList() {
        Page<CountryListEntity> page = Mockito.mock(Page.class);
        when(page.hasContent()).thenReturn(false);

        when(countryListRepository.findAll(any(Pageable.class))).thenReturn(page);
        List<CountryListEntity> list = countryListService.getList(1, 20, "asd");

        assertEquals(list, new ArrayList<>());
    }

    @Test
    public void DeleteCountryList() {
        when(countryListRepository.getById(anyLong())).thenReturn(Optional.of(mockCountryListEntity()));

        countryListService.delete(COUNTRY_LIST_ID);
        verify(countryListRepository).getById(COUNTRY_LIST_ID);
    }

    @Test
    public void AddCountry() {

        CountryEntity mockCountryEntity = new CountryEntity().setName(COUNTRY_NAME).setIsoCountryCode(COUNTRY_ISO);
        CountryListEntity mockCountryListEntity = new CountryListEntity().setName(COUNTRY_LIST_NAME).setCountries(new HashSet<>());

        when(countryService.get(anyLong())).thenReturn(mockCountryEntity);
        when(countryListRepository.getById(anyLong())).thenReturn(Optional.of(mockCountryListEntity));
        when(countryListRepository.save(any(CountryListEntity.class)))
                .thenReturn(any(CountryListEntity.class));

        CountryListEntity res = countryListService.addCountry(COUNTRY_LIST_ID,COUNTRY_ID);

        verify(countryListRepository).save(any(CountryListEntity.class));

    }

    @Test
    public void Compare() {
        CountryEntity countryEntity1 = new CountryEntity().setId(1L).setName("Argentina");
        CountryEntity countryEntity2 = new CountryEntity().setId(2L).setName("Brasil");
        CountryEntity countryEntity3 = new CountryEntity().setId(3L).setName("Chile");
        CountryEntity countryEntity4 = new CountryEntity().setId(4L).setName("Uruguay");
        CountryEntity countryEntity5 = new CountryEntity().setId(5L).setName("Bolivia");
        CountryListEntity countryList1 = new CountryListEntity().setId(1L).setName("Lista_1").setCountries(new HashSet<>());
        CountryListEntity countryList2 = new CountryListEntity().setId(2L).setName("Lista_2").setCountries(new HashSet<>());
        countryList1.getCountries().add(countryEntity1);
        countryList1.getCountries().add(countryEntity2);
        countryList1.getCountries().add(countryEntity3);
        countryList1.getCountries().add(countryEntity4);
        countryList2.getCountries().add(countryEntity3);
        countryList2.getCountries().add(countryEntity4);
        countryList2.getCountries().add(countryEntity5);

        when(countryListRepository.getById(1L)).thenReturn(Optional.of(countryList1));
        when(countryListRepository.getById(2L)).thenReturn(Optional.of(countryList2));

        List<CountryEntity> listRes = countryListService.compare(1L,2L);
        assertEquals(2,listRes.size());

    }

    @Test
    public void Compare_NotFoundException() {
        CountryEntity countryEntity1 = new CountryEntity().setId(1L).setName("Argentina");
        CountryListEntity countryList1 = new CountryListEntity().setId(1L).setName("Lista_1").setCountries(new HashSet<>());
        countryList1.getCountries().add(countryEntity1);

        when(countryListRepository.getById(1L)).thenReturn(Optional.of(countryList1));
        when(countryListRepository.getById(2L)).thenReturn(Optional.empty());

        assertThrows (
            NotFoundException.class, () -> countryListService.compare(1L, 2L)
        );

        assertThrows (
                NotFoundException.class, () -> countryListService.compare(2L, 1L)
        );
    }

    @Test
    public void getInfo() {

        CountryListEntity countryList1 = new CountryListEntity().setId(1L).setName("Lista_1").setCountries(new HashSet<>());
        CountryListEntity countryList2 = new CountryListEntity().setId(2L).setName("Lista_2").setCountries(new HashSet<>());
        List<CountryListEntity> lists = new ArrayList<CountryListEntity>();
        lists.add(countryList1);
        lists.add(countryList2);

        when(countryListRepository.findByCreationDateBetween(any(Date.class), any(Date.class))).thenReturn(lists);
        when(countryListRepository.findAllByIdIsNotNull()).thenReturn(lists);

        CountryListsStats res = null;

        res = countryListService.getInfo(2);
        assertEquals(lists.size(), res.getCount());

        res = countryListService.getInfo(-2);
        assertEquals(lists.size(), res.getCount());
    }
}
