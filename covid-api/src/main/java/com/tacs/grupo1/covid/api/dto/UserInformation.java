package com.tacs.grupo1.covid.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInformation {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("telegram_id")
    private Long telegramId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("total_lists")
    private long totalLists;
    @JsonProperty("total_countries")
    private long totalCountries;
    @JsonProperty("last_login")
    private LocalDateTime lastLogin;
}
