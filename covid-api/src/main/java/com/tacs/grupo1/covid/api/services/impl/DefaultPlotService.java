package com.tacs.grupo1.covid.api.services.impl;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.statistics.CountryListPlot;
import com.tacs.grupo1.covid.api.dto.statistics.CountryPlot;
import com.tacs.grupo1.covid.api.dto.statistics.StatisticsDay;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryTimeSeries;
import com.tacs.grupo1.covid.api.repositories.CovidRepository;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.CountryService;
import com.tacs.grupo1.covid.api.services.PlotService;
import com.tacs.grupo1.covid.api.util.ConvertCountry;
import com.tacs.grupo1.covid.api.util.ConvertStatisticsDay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("plotService")
public class DefaultPlotService implements PlotService {

    private final CovidRepository covidRepository;

    private final CountryService countryService;

    private final CountryListService countryListService;

    @Autowired
    public DefaultPlotService(CovidRepository covidRepository, CountryService countryService, CountryListService countryListService) {
        this.covidRepository = covidRepository;
        this.countryService = countryService;
        this.countryListService = countryListService;
    }

    public CountryPlot getCountryPlot(long countryId) {
        CountryEntity country = countryService.get(countryId);
        List<CovidCountryTimeSeries> timeSeries = new ArrayList<CovidCountryTimeSeries>();

        timeSeries = covidRepository.getCountryTimeSeries(country.getIsoCountryCode());

        return new CountryPlot()
                .setCountry(ConvertCountry.entityToDto(country))
                .setStatisticsDayList(retrieveStatisticsDayList(timeSeries));
    }

    @Override
    public CountryListPlot getCountryListPlot(long countryListId, long days) {
        CountryListEntity countryListEntity = countryListService.get(countryListId);
        CountryListPlot countryListPlot = new CountryListPlot()
                .setId(countryListEntity.getId()).setName(countryListEntity.getName())
                .setCountryPlotList(
                        countryListEntity.getCountries().stream()
                                .map(countryEntity -> getCountryPlot(countryEntity.getId()))
                                .collect(Collectors.toList())
                );

        if (days < 0) {  // Statistics from last X days
            List<CountryPlot> aux = countryListPlot.getCountryPlotList();

            int i = 0;
            for (CountryPlot cp : aux) {
                int totalDays = cp.getStatisticsDayList().size();

                List<StatisticsDay> auxList =
                        cp.getStatisticsDayList().subList((int) (totalDays + days), totalDays);

                countryListPlot.getCountryPlotList().get(i).setStatisticsDayList(auxList);
                i++;
            }

        } else if (days > 0) {    // Statistics from the first X days

            List<CountryPlot> aux = countryListPlot.getCountryPlotList();
            int i = 0;
            for (CountryPlot cp : aux) {
                long firstDay = cp.getCountry().getOffsetVal() + days - 1;
                int totalDays = cp.getStatisticsDayList().size();

                List<StatisticsDay> auxList =
                        cp.getStatisticsDayList().subList((int) firstDay, totalDays);

                countryListPlot.getCountryPlotList().get(i).setStatisticsDayList(auxList);
                i++;
            }
        }
        return countryListPlot;
    }

    @Override
    public CountryListPlot getCountryListPlotOnTheFly(CountryList countryList) {

        CountryListPlot countryListPlot = new CountryListPlot()
                .setId(99999L).setName("StrategyPlot")
                .setCountryPlotList(
                        countryList.getCountries().stream()
                                .map(country -> getCountryPlot(countryService.getByIsoCountryCode(country.getIsoCountryCode()).getId()))
                                .collect(Collectors.toList())
                );

        return countryListPlot;
    }

    private List<StatisticsDay> retrieveStatisticsDayList(List<CovidCountryTimeSeries> timeSeries) {
        List<StatisticsDay> statisticsDays = new ArrayList<>();
        timeSeries.get(0).getTimeseries().forEach((date, statistics) -> {
            statisticsDays.add(ConvertStatisticsDay.dtoStatisticsToDto(date, statistics));
        });
        return statisticsDays;
    }
}
