package com.tacs.grupo1.covid.api.util;

import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.CountryList;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class ConvertCountryList {

    public static CountryListEntity dtoToEntity(CountryList countryList) {
        Date dateCreation=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        if (!(countryList.getCreationDate() == null)){
            if (!(countryList.getCreationDate().isEmpty())) {
                try {
                    dateCreation = dateFormat.parse(countryList.getCreationDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new CountryListEntity()
                .setId(countryList.getId())
                .setName(countryList.getName())
                .setUser(new UserEntity().setId(countryList.getUserId()))
                .setCreationDate(dateCreation)
                .setCountries(countryList.getCountries().stream().map(ConvertCountry::dtoToEntity).collect(Collectors.toSet()));
    }

    public static CountryList entityToDto(CountryListEntity countryList) {
            return new CountryList()
                    .setId(countryList.getId())
                    .setName(countryList.getName())
                    .setUserId(countryList.getUser().getId())
                    .setCreationDate(countryList.getCreationDate().toString())
                    .setCountries(countryList.getCountries().stream().map(ConvertCountry::entityToDto).collect(Collectors.toSet()));
    }
}
