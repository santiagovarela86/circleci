package com.tacs.grupo1.covid.api.dto;

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
public class CountryInterest {
    private long idCountry;
    private String name;
    private String isoCode;
    private long totalLists;
}
