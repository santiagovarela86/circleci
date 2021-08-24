package com.tacs.grupo1.covid.api.utils;

import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;

public class BaseConvertTest {

    public static final long USER_ID = 1L;
    public static final String USER_USERNAME = "pepeusr";
    public static final String USER_PASSWORD = "123456";
    public static final String USER_NAME = "Pepe";
    public static final String USER_LASTNAME = "Argento";
    public static final String USER_EMAIL = "pepe@gmail.com";

    public static final long COUNTRY_ID = 1;
    public static final String COUNTRY_NAME = "Argentina";
    public static final String COUNTRY_ISO = "AR";
    public static final OffsetDateTime COUNTRY_STARTDATE = null;
    public static final long COUNTRY_OFFSET = 3;


    public static final long COUNTRY_LIST_ID = 1;
    public static final String COUNTRY_LIST_NAME = "Lista de Pepe";


    public BaseConvertTest() {
        MockitoAnnotations.initMocks(this);
    }
}
