package com.tacs.grupo1.covid.api.util;

import com.tacs.grupo1.covid.api.dto.statistics.Statistics;
import com.tacs.grupo1.covid.api.dto.statistics.StatisticsDay;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class ConvertStatisticsDay {
    public static StatisticsDay dtoStatisticsToDto(String day, Statistics statistics) {
        return new StatisticsDay()
                .setDate(LocalDate.parse(day, DateTimeFormatter.ofPattern("M/d/yy"))
                        .atStartOfDay(ZoneId.of("America/Buenos_Aires")).toOffsetDateTime())
                .setConfirmed(statistics.getConfirmed())
                .setDeaths(statistics.getDeaths())
                .setRecovered(statistics.getRecovered());
    }
}
