package com.tacs.grupo1.covid.api.dto.statistics.covid;

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
public class CovidCountryCode {
    private String iso2;
    private String iso3;
}
