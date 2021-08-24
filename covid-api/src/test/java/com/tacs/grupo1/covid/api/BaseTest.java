package com.tacs.grupo1.covid.api;

import org.mockito.MockitoAnnotations;

public class BaseTest {

    public static final long USER_ID = 1L;
    public static final String USER_NAME = "pepe";

    public static final long COUNTRY_ID = 1;
    public static final String COUNTRY_NAME = "Argentina";
    public static final String COUNTRY_ISO = "AR";

    public static final long COUNTRY_LIST_ID = 1;
    public static final String COUNTRY_LIST_NAME = "Lista de Pepe";

    public BaseTest() {
        MockitoAnnotations.initMocks(this);
    }
}
