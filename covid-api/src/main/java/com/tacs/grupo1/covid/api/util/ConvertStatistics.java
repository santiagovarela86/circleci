package com.tacs.grupo1.covid.api.util;

import com.tacs.grupo1.covid.api.dto.statistics.Statistics;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryStatistics;
import org.springframework.stereotype.Component;

@Component
public class ConvertStatistics {

    public static Statistics dtoCovidToDto(CovidCountryStatistics statistics) {
        return new Statistics()
                .setConfirmed(statistics.getConfirmed())
                .setDeaths(statistics.getDeaths())
                .setRecovered(statistics.getRecovered());
    }
}