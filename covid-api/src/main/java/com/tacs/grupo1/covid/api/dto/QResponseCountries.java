package com.tacs.grupo1.covid.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QResponseCountries {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("list_name")
    private String listName;
    @JsonProperty("country_name")
    private String countryName;

    public QResponseCountries(String userName) {
        this.userName = userName;
    }
}