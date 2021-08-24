package com.tacs.grupo1.covid.api.dto.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tacs.grupo1.covid.api.dto.CountryList;
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
public class CountryListPlot {
    private Long id;
    private String name;
    @JsonProperty("country_plot_list")
    private List<CountryPlot> countryPlotList;
}
