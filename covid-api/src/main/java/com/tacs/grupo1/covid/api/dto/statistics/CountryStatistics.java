package com.tacs.grupo1.covid.api.dto.statistics;

import com.tacs.grupo1.covid.api.dto.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CountryStatistics {
    private Country country;
    private Statistics statistics;
}
