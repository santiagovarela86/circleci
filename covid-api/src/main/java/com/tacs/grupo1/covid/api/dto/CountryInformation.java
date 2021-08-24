package com.tacs.grupo1.covid.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CountryInformation {
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("total_users")
    private long totalUsers;
    @JsonProperty("users_names")
    private List<String> usersNames;
}
