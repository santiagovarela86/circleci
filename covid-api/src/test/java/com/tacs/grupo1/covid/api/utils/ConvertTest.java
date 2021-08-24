package com.tacs.grupo1.covid.api.utils;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.UserEntity;
import com.tacs.grupo1.covid.api.dto.Country;
import com.tacs.grupo1.covid.api.dto.User;
import com.tacs.grupo1.covid.api.util.ConvertCountry;
import com.tacs.grupo1.covid.api.util.ConvertUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertTest extends BaseConvertTest {

    @Test
    @DisplayName("ConvertUser_entityToDto_OK")
    void convertUser_entityToDto() {

        ConvertUser convertUser = new ConvertUser(null);

        UserEntity userEntity = new UserEntity()
                .setId(USER_ID)
                .setUserName(USER_USERNAME)
                .setPassword(USER_PASSWORD)
                .setName(USER_NAME)
                .setLastName(USER_LASTNAME)
                .setEmail(USER_EMAIL);

        User res = convertUser.entityToDto(userEntity);

        assertEquals(USER_ID, res.getId());
        assertEquals(USER_USERNAME, res.getUserName());
        assertEquals("*Protected*", res.getPassword());
        assertEquals(USER_NAME, res.getName());
        assertEquals(USER_LASTNAME, res.getLastName());
        assertEquals(USER_EMAIL, res.getEmail());

    }

    @Test
    @DisplayName("ConvertUser_dtoToEntity_OK")
    void convertUser_dtoToEntity() {

        ConvertUser convertUser = new ConvertUser(null);

        User user = new User()
                .setId(USER_ID)
                .setUserName(USER_USERNAME)
                .setPassword(USER_PASSWORD)
                .setName(USER_NAME)
                .setLastName(USER_LASTNAME)
                .setEmail(USER_EMAIL);


        UserEntity res = convertUser.dtoToEntity(user);

        assertEquals(USER_ID, res.getId());
        assertEquals(USER_USERNAME, res.getUserName());
        assertEquals(USER_PASSWORD, res.getPassword());
        assertEquals(USER_NAME, res.getName());
        assertEquals(USER_LASTNAME, res.getLastName());
        assertEquals(USER_EMAIL, res.getEmail());

    }

    @Test
    @DisplayName("ConvertCountry_dtoToEntity_OK")
    void convertCountry_dtoToEntity() {

        ConvertCountry convertCountry = new ConvertCountry();

        Country country = new Country()
                .setId(COUNTRY_ID)
                .setName(COUNTRY_NAME)
                .setIsoCountryCode(COUNTRY_ISO)
                .setStartDate(COUNTRY_STARTDATE)
                .setOffsetVal(COUNTRY_OFFSET);

        CountryEntity res = convertCountry.dtoToEntity(country);

        assertEquals(COUNTRY_ID, res.getId());
        assertEquals(COUNTRY_NAME, res.getName());
        assertEquals(COUNTRY_ISO, res.getIsoCountryCode());
        assertEquals(COUNTRY_STARTDATE, res.getStartDate());
        assertEquals(COUNTRY_OFFSET, res.getOffsetVal());

    }

    @Test
    @DisplayName("ConvertCountry_entityToDto_OK")
    void convertCountry_entityToDto() {

        ConvertCountry convertCountry = new ConvertCountry();

        CountryEntity countryEntity = new CountryEntity()
                .setId(COUNTRY_ID)
                .setName(COUNTRY_NAME)
                .setIsoCountryCode(COUNTRY_ISO)
                .setStartDate(COUNTRY_STARTDATE)
                .setOffsetVal(COUNTRY_OFFSET);


        Country res = convertCountry.entityToDto(countryEntity);

        assertEquals(COUNTRY_ID, res.getId());
        assertEquals(COUNTRY_NAME, res.getName());
        assertEquals(COUNTRY_ISO, res.getIsoCountryCode());
        assertEquals(COUNTRY_STARTDATE, res.getStartDate());
        assertEquals(COUNTRY_OFFSET, res.getOffsetVal());

    }



}
