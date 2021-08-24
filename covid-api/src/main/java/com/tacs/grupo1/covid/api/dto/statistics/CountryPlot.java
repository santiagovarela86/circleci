package com.tacs.grupo1.covid.api.dto.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tacs.grupo1.covid.api.dto.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CountryPlot {
    private Country country;
    @JsonProperty("statistics_day_list")
    private List<StatisticsDay> statisticsDayList;
}
