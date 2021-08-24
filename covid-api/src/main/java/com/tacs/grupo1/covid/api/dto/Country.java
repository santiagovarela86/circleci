package com.tacs.grupo1.covid.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Country {
    private Long id;
    private String name;
    @JsonProperty("iso_country_code")
    private String isoCountryCode;
    @JsonProperty("start_date")
    private OffsetDateTime startDate;
    @JsonProperty("offset")
    private Long offsetVal;
    @JsonProperty("strategy")
    private String strategy;

}
