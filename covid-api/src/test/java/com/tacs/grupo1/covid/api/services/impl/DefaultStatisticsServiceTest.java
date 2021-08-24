package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.domain.RoleEntity;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.dto.statistics.CountryListStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.CountryStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.Statistics;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryCode;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryTimeSeries;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidLocation;
import com.tacs.grupo1.covid.api.repositories.CovidRepository;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.tacs.grupo1.covid.api.utils.CountryListUtils.mockCountryListEntity;
import static com.tacs.grupo1.covid.api.utils.CountryUtils.mockCountryEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DefaultStatisticsServiceTest extends BaseTest {

    @Mock
    CovidRepository covidRepository;

    @Mock
    DefaultCountryListService countryListService;

    @Mock
    DefaultCountryService countryService;

    @Autowired
    @InjectMocks
    DefaultStatisticsService service;

    CountryEntity countryEntity = mockCountryEntity().setIsoCountryCode(COUNTRY_ISO);
    CountryListEntity countryListEntity = mockCountryListEntity();
    List<CovidCountryStatistics> covidCountryStatistics = new ArrayList<>();

    @Test
    @DisplayName("get statistics for country")
    void getStatisticsForCountry() {
        covidCountryStatistics.add(new CovidCountryStatistics());
        when(countryService.get(anyLong())).thenReturn(countryEntity);
        when(covidRepository.getCountryStats(anyString())).thenReturn(covidCountryStatistics);

        CountryStatistics statistics = service.getCountryStatistics(1L);

        assertEquals(1L, statistics.getCountry().getId());
    }

    @Test
    @DisplayName("get statistics for country")
    void getCountryListStatistics() {
        countryListEntity.setId(COUNTRY_LIST_ID).setName("asd").setCountries(new HashSet<>());
        when(countryListService.get(anyLong())).thenReturn(countryListEntity);
        CountryListStatistics statistics = service.getCountryListStatistics(1L);

        assertEquals(1L, statistics.getId());
    }

    @Test
    void getCountryStartDay() {
        when(covidRepository.getCountryTimeSeries(anyString())).thenReturn(new ArrayList<>());
    }

    @Test
    void initializeDB() {

        List<CovidCountryTimeSeries> listTimeSeries = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo = new CovidCountryTimeSeries();
        dataInfo
                .setCountryregion("Argentina")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("AR").setIso3("ARG"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries.add(dataInfo);

        List<CountryEntity> countries = new ArrayList<CountryEntity>();
        CountryEntity countryEntity = new CountryEntity().setId(1L).setName("Argentina").setIsoCountryCode("AR");
        countries.add(countryEntity);
        CountryEntity countryEntity2 = new CountryEntity().setId(2L).setName("Chile").setIsoCountryCode("CL");
        countries.add(countryEntity2);

        when(countryService.getAll()).thenReturn(countries);
        when(covidRepository.getCountryTimeSeries(anyString())).thenReturn(listTimeSeries);
        when(countryService.update(anyLong(),any(CountryEntity.class))).thenReturn(countryEntity);

        service.initializeDB();

    }


    @Test
    void getInitialStats() {

        List<CovidCountryTimeSeries> listTimeSeries = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo = new CovidCountryTimeSeries();
        dataInfo
                .setCountryregion("Argentina")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("AR").setIso3("ARG"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries.add(dataInfo);

        List<CountryEntity> countries = new ArrayList<CountryEntity>();
        CountryEntity countryEntity = new CountryEntity().setId(1L).setName("Argentina").setIsoCountryCode("AR");
        countries.add(countryEntity);

        when(covidRepository.getCountryTimeSeries("AR")).thenReturn(listTimeSeries);

        service.getInitialStats(countryEntity);

    }

}
