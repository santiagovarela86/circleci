package com.tacs.grupo1.covid.api.util;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.Country;
import org.springframework.stereotype.Component;

@Component
public class ConvertCountry {

    public static CountryEntity dtoToEntity(Country country) {
        return new CountryEntity()
                .setId(country.getId())
                .setName(country.getName())
                .setIsoCountryCode(country.getIsoCountryCode())
                .setStartDate(country.getStartDate())
                .setOffsetVal(country.getOffsetVal())
                .setStrategy(country.getStrategy());
    }

    public static Country entityToDto(CountryEntity country) {
        return new Country()
                .setId(country.getId())
                .setName(country.getName())
                .setIsoCountryCode(country.getIsoCountryCode())
                .setStartDate(country.getStartDate())
                .setOffsetVal(country.getOffsetVal())
                .setStrategy(country.getStrategy());
    }
}
