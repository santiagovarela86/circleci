package com.tacs.grupo1.covid.api.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.covid.CovidCountryTimeSeries;
import com.tacs.grupo1.covid.api.exceptions.RestRepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Repository
public class CovidRepository extends AbstractRestRepository {

    @Value("${covidapiaddr}")
    private String covidapiaddr;
    @Value("${countryloadapiaddr}")
    private String countryloadapiaddr;

    public CovidRepository(RestTemplateBuilder restBuilder) {
        super(restBuilder);
    }

    public List<CovidCountryStatistics> getCountryStats(String countryCode) {
        try {
            String uri = buildCountryStaticsUrl(countryCode);
            ResponseEntity<String> response = get(uri, String.class);
            String body = response.getBody();
            if (nonNull(body)) {
                CovidCountryStatistics[] data = new ObjectMapper().readValue(body, CovidCountryStatistics[].class);
                return Arrays.asList(data);
            }
        } catch (JsonProcessingException e) {
            log.error("Json Processing Error", e);
        }
        throw new RestRepositoryException("Error getting Country Stats");
    }

    public List<CovidCountryTimeSeries> getCountryTimeSeries(String countryCode) {
        try {
            String uri = buildCountryStaticsTimeSeries(countryCode);
            ResponseEntity<String> response = get(uri, String.class);
            String body = response.getBody();
            if (nonNull(body)) {
                CovidCountryTimeSeries[] data = new ObjectMapper().readValue(body, CovidCountryTimeSeries[].class);
                return Arrays.asList(data);
            }
        } catch (JsonProcessingException e) {
            log.error("Json Processing Error", e);
        }
        throw new RestRepositoryException("Error getting Country Time Series");
    }

    private String buildGlobalStaticsUrl() {
        return getUriBuilder(getBaseUri() + "/jhu-edu/brief").toUriString();
    }

    private String buildCountryStaticsUrl(String countryCode) {
        return getUriBuilder(getBaseUri() + "/jhu-edu/latest")
                .queryParam("iso2", countryCode)
                .queryParam("onlyCountries", false)
                .toUriString();
    }

    private String buildCountryStaticsTimeSeries(String countryCode) {
        return getUriBuilder(getBaseUri() + "/jhu-edu/timeseries")
                .queryParam("iso2", countryCode)
                .queryParam("onlyCountries", false)
                .toUriString();
    }

    private String getBaseUri() {
        return this.covidapiaddr;
    }

    public List<CountryEntity> getAllCountries() {
        List<CountryEntity> countries = new ArrayList<CountryEntity>();

        ResponseEntity<String> response = get(countryloadapiaddr, String.class);
        String body = response.getBody();
        if (nonNull(body)) {

            JSONArray jArray = new JSONArray(body);
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsoncountry = jArray.getJSONObject(i);
                    String countryCode = jsoncountry.getString("alpha2Code");
                    JSONObject translations_array = jsoncountry.getJSONObject("translations");
                    String countryName = "";
                    try {
                        countryName = translations_array.getString("es");
                    } catch (org.json.JSONException e) {
                        countryName = jsoncountry.getString("name");
                    }
                    CountryEntity country = new CountryEntity()
                            .setName(countryName)
                            .setIsoCountryCode(countryCode);
                    countries.add(country);
                }
                return countries;
            }
        }
        log.error("Null countries response.");
        return countries;
    }
}