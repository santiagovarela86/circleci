package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.statistics.CountryListStatistics;
import com.tacs.grupo1.covid.api.dto.statistics.CountryStatistics;

import java.time.OffsetDateTime;

public interface StatisticsService {

    public CountryStatistics getCountryStatistics(long countryId);

    public CountryListStatistics getCountryListStatistics(long countryListId);

    public OffsetDateTime getCountryStartDay (String countryCode);

    public Long getCountryOffset (String countryCode);

    public void getInitialStats(CountryEntity country);

    public void initializeDB();
}
