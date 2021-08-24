package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.dto.statistics.CountryListStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.CountryStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.Statistics;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryTimeSeries;
import com.tacs.grupo1.covid.api.repositories.CovidRepository;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.CountryService;
import com.tacs.grupo1.covid.api.services.StatisticsService;
import com.tacs.grupo1.covid.api.util.ConvertCountry;
import com.tacs.grupo1.covid.api.util.ConvertStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("statisticsService")
public class DefaultStatisticsService implements StatisticsService {

    private final CovidRepository covidRepository;

    private final CountryService countryService;

    private final CountryListService countryListService;

    @Autowired
    public DefaultStatisticsService(CovidRepository covidRepository, CountryService countryService, CountryListService countryListService) {
        this.covidRepository = covidRepository;
        this.countryService = countryService;
        this.countryListService = countryListService;
    }

    @Override
    public CountryStatistics getCountryStatistics(long countryId) {
        CountryEntity country = countryService.get(countryId);

        List<CovidCountryStatistics> statistics = covidRepository.getCountryStats(country.getIsoCountryCode());

        return new CountryStatistics()
                .setCountry(ConvertCountry.entityToDto(country))
                .setStatistics(ConvertStatistics.dtoCovidToDto(statistics.get(0)));
    }

    @Override
    public CountryListStatistics getCountryListStatistics(long countryListId) {
        CountryListEntity countryList = countryListService.get(countryListId);
        return new CountryListStatistics()
                .setId(countryList.getId())
                .setName(countryList.getName())
                .setCountryStatisticsList(
                        countryList.getCountries().stream()
                                .map(countryEntity -> getCountryStatistics(countryEntity.getId()))
                                .collect(Collectors.toList())
                );
    }


    @Override
    public OffsetDateTime getCountryStartDay(String countryCode) {
        List<CovidCountryTimeSeries> listTimeSeries = covidRepository.getCountryTimeSeries(countryCode);

        if (listTimeSeries.size() != 0) {
            String startDate = "";
            for (Map.Entry<String, Statistics> timeSerie : listTimeSeries.get(0).getTimeseries().entrySet()) {

                Statistics valor = timeSerie.getValue();

                if (valor.getConfirmed() > 0) {
                    startDate = timeSerie.getKey();
                    break;
                }
            }

            return LocalDate.parse(startDate, DateTimeFormatter.ofPattern("M/d/yy"))
                    .atStartOfDay(ZoneId.of("America/Buenos_Aires")).toOffsetDateTime();
        } else return null;
    }

    @Override
    public Long getCountryOffset(String countryCode) {
        List<CovidCountryTimeSeries> listTimeSeries = covidRepository.getCountryTimeSeries(countryCode);

        if (listTimeSeries.size() != 0) {
            long offset = 0;
            for (Map.Entry<String, Statistics> timeSerie : listTimeSeries.get(0).getTimeseries().entrySet()) {

                if (timeSerie.getValue().getConfirmed() > 0) {
                    break;
                }
                offset++;
            }
            return offset;
        } else {
            return null;
        }
    }


    @Override
    public void getInitialStats(CountryEntity country) {
        List<CovidCountryTimeSeries> listTimeSeries = covidRepository.getCountryTimeSeries(country.getIsoCountryCode());

        if (listTimeSeries.size() != 0) {
            String startDate = "";
            for (Map.Entry<String, Statistics> timeSerie : listTimeSeries.get(0).getTimeseries().entrySet()) {

                Statistics valor = timeSerie.getValue();

                if (valor.getConfirmed() > 0) {
                    startDate = timeSerie.getKey();
                    break;
                }
            }
            country.setStartDate(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("M/d/yy")).atStartOfDay(ZoneId.of("America/Buenos_Aires")).toOffsetDateTime());

            long offset = 0;
            for (Map.Entry<String, Statistics> timeSerie : listTimeSeries.get(0).getTimeseries().entrySet()) {

                if (timeSerie.getValue().getConfirmed() > 0) {
                    break;
                }
                offset++;
            }
            country.setOffsetVal(offset);
        }
    }

    @Override
    public void initializeDB() {
        List<CountryEntity> countries = countryService.getAll();

        countries.parallelStream().forEach(country -> {
            log.info("Calculando estadísticas de: " + country.getName());
            this.getInitialStats(country);
        });
        countries.parallelStream().forEach(country -> {
            countryService.update(country.getId(), country);
        });

    }
}
