package com.tacs.grupo1.covid.api.websecurity;

import java.io.Serializable;

public class JwtResponse implements Serializable
{

    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}