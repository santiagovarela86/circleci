package com.tacs.grupo1.covid.api.dto.statistics.covid;

import com.tacs.grupo1.covid.api.dto.statistics.Statistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Accessors(chain = true)
public class CovidCountryStatistics extends Statistics {
    private String countryregion;
    private String lastupdate;
    private CovidLocation location;
    private CovidCountryCode countrycode;
}
