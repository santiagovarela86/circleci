package com.tacs.grupo1.covid.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CountryList {
    private Long id;
    private String name;
    private Set<Country> countries;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("creation_date")
    private String creationDate;
}
