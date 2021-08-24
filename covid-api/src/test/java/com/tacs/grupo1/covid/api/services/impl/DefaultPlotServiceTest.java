package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.BaseTest;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.statistics.*;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryCode;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryTimeSeries;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidLocation;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.repositories.CovidRepository;
import com.tacs.grupo1.covid.api.repositories.UserRepository;
import org.apache.logging.log4j.CloseableThreadContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DefaultPlotServiceTest extends BaseTest {

    @InjectMocks
    DefaultPlotService plotService;

    @Mock
    DefaultCountryListService countryListService;

    @Mock
    DefaultCountryService countryService;

    @Mock
    CovidRepository covidRepository;

    @Test
    @DisplayName("getCountryListPlot")
    void getCountryListPlot() {

        CountryEntity countryEntity1 = new CountryEntity().setName("Argentina").setIsoCountryCode("AR").setId(1l).setOffsetVal(1L);
        CountryEntity countryEntity2 = new CountryEntity().setName("Chile").setIsoCountryCode("CL").setId(2l).setOffsetVal(1L);
        CountryEntity countryEntity3 = new CountryEntity().setName("Brasil").setIsoCountryCode("BR").setId(3l).setOffsetVal(1L);

        CountryListEntity countryListEntity = new CountryListEntity().setId(1L).setName("List_1_Test").setCountries(new HashSet<CountryEntity>());
        countryListEntity.getCountries().add(countryEntity1);
        countryListEntity.getCountries().add(countryEntity2);
        countryListEntity.getCountries().add(countryEntity3);

        List<CovidCountryTimeSeries> listTimeSeries1 = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo1 = new CovidCountryTimeSeries();
        dataInfo1
                .setCountryregion("Argentina")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("AR").setIso3("ARG"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo1.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo1.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo1.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries1.add(dataInfo1);

        List<CovidCountryTimeSeries> listTimeSeries2 = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo2 = new CovidCountryTimeSeries();
        dataInfo2
                .setCountryregion("Chile")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("CL").setIso3("CL"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo2.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo2.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo2.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries2.add(dataInfo2);

        List<CovidCountryTimeSeries> listTimeSeries3 = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo3 = new CovidCountryTimeSeries();
        dataInfo3
                .setCountryregion("Brasil")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("BR").setIso3("BR"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo3.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo3.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo3.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries3.add(dataInfo3);

        when(countryListService.get(anyLong())).thenReturn(countryListEntity);
        when(countryService.get(1L)).thenReturn(countryEntity1);
        when(countryService.get(2L)).thenReturn(countryEntity2);
        when(countryService.get(3L)).thenReturn(countryEntity3);
        when(covidRepository.getCountryTimeSeries("AR")).thenReturn(listTimeSeries1);
        when(covidRepository.getCountryTimeSeries("CL")).thenReturn(listTimeSeries2);
        when(covidRepository.getCountryTimeSeries("BR")).thenReturn(listTimeSeries3);

        CountryListPlot res1 = plotService.getCountryListPlot(1L,0L);
        System.out.println("plotService.getCountryListPlot(1L,0L).getId()=" + res1.getId());
        assertEquals(1,res1.getId());
        System.out.println("plotService.getCountryListPlot(1L,0L).getName()=" + res1.getName());
        assertEquals("List_1_Test",res1.getName());
        System.out.println("plotService.getCountryListPlot(1L,0L).size()=" + res1.getCountryPlotList().size());
        assertEquals(3,res1.getCountryPlotList().size());
        CountryListPlot res2 = plotService.getCountryListPlot(1L,1L);
        System.out.println("plotService.getCountryListPlot(1L,1L).size()=" + res2.getCountryPlotList().size());
        assertEquals(3,res2.getCountryPlotList().size());
        CountryListPlot res3 = plotService.getCountryListPlot(1L,-2L);
        System.out.println("plotService.getCountryListPlot(1L,-2L).size()=" + res3.getCountryPlotList().size());
        assertEquals(3,res3.getCountryPlotList().size());

    }

    @Test
    @DisplayName("getCountryListPlotOnTheFly")
    void getCountryListPlotOnTheFly() {

        CountryList countryListIn = new CountryList().setName("List Test").setCountries(new HashSet<Country>());
        countryListIn.getCountries().add(new Country().setId(1L).setName("Argentina").setIsoCountryCode("AR"));
        countryListIn.getCountries().add(new Country().setId(2L).setName("Chile").setIsoCountryCode("CL"));
        countryListIn.getCountries().add(new Country().setId(3L).setName("Brasil").setIsoCountryCode("BR"));

        CountryEntity countryEntity1 = new CountryEntity().setName("Argentina").setIsoCountryCode("AR").setId(1l);
        CountryEntity countryEntity2 = new CountryEntity().setName("Chile").setIsoCountryCode("CL").setId(2l);
        CountryEntity countryEntity3 = new CountryEntity().setName("Brasil").setIsoCountryCode("BR").setId(3l);

        List<CovidCountryTimeSeries> listTimeSeries1 = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo1 = new CovidCountryTimeSeries();
        dataInfo1
                .setCountryregion("Argentina")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("AR").setIso3("ARG"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo1.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo1.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo1.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries1.add(dataInfo1);

        List<CovidCountryTimeSeries> listTimeSeries2 = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo2 = new CovidCountryTimeSeries();
        dataInfo2
                .setCountryregion("Chile")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("CL").setIso3("CL"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo2.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo2.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo2.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries2.add(dataInfo2);

        List<CovidCountryTimeSeries> listTimeSeries3 = new ArrayList<CovidCountryTimeSeries>();
        CovidCountryTimeSeries dataInfo3 = new CovidCountryTimeSeries();
        dataInfo3
                .setCountryregion("Brasil")
                .setLastupdate("2020-06-22T05:42:00.004Z")
                .setLocation(new CovidLocation().setLat(""+ (-38.4161)).setLng(""+(-63.6167)))
                .setCountrycode(new CovidCountryCode().setIso2("BR").setIso3("BR"))
                .setTimeseries(new TreeMap<String, Statistics>());
        dataInfo3.getTimeseries().put("6/20/20", new Statistics().setConfirmed(0L).setDeaths(0L).setRecovered(0L));
        dataInfo3.getTimeseries().put("6/21/20", new Statistics().setConfirmed(1L).setDeaths(0L).setRecovered(0L));
        dataInfo3.getTimeseries().put("6/22/20", new Statistics().setConfirmed(5L).setDeaths(1L).setRecovered(2L));
        listTimeSeries3.add(dataInfo3);


        when(countryService.getByIsoCountryCode("AR")).thenReturn(countryEntity1);
        when(countryService.getByIsoCountryCode("CL")).thenReturn(countryEntity2);
        when(countryService.getByIsoCountryCode("BR")).thenReturn(countryEntity3);

        when(countryService.get(1L)).thenReturn(countryEntity1);
        when(countryService.get(2L)).thenReturn(countryEntity2);
        when(countryService.get(3L)).thenReturn(countryEntity3);

        when(covidRepository.getCountryTimeSeries("AR")).thenReturn(listTimeSeries1);
        when(covidRepository.getCountryTimeSeries("CL")).thenReturn(listTimeSeries2);
        when(covidRepository.getCountryTimeSeries("BR")).thenReturn(listTimeSeries3);


        CountryListPlot res = plotService.getCountryListPlotOnTheFly(countryListIn);


        System.out.println("Prueba res.getName()= " + res.getName());
        System.out.println("Prueba res.getId()= " + res.getId());
        System.out.println("Prueba res.getCountryPlotList().size()= " + res.getCountryPlotList().size());
        System.out.println("Prueba Argentina,Chile,Brasil contains res.getCountryPlotList().get(1).getCountry().getName()= " + res.getCountryPlotList().get(1).getCountry().getName());

        assertEquals("StrategyPlot",res.getName());
        assertEquals(99999,res.getId());
        assertEquals(3,res.getCountryPlotList().size());
        assertTrue("Argentina,Chile,Brasil".contains(res.getCountryPlotList().get(1).getCountry().getName()));

    }
/*
    @Test
    @DisplayName("get statistics for country")
    void getStatisticsForCountry() {

        CountryPlot plot = service.getCountryPlot(1L);

        assertEquals(1L, plot.getCountry().getId());
    }

    @Test
    @DisplayName("get statistics for country")
    void getCountryListStatistics() {

        CountryListPlot listPlot = service.getCountryListPlot(1L);

        assertEquals(1L, listPlot.getId());
    }*/
}
