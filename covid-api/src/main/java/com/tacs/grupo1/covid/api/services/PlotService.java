package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.dto.CountryList;
import com.tacs.grupo1.covid.api.dto.statistics.CountryListPlot;
import com.tacs.grupo1.covid.api.dto.statistics.CountryPlot;

public interface PlotService {
    public CountryPlot getCountryPlot(long countryId);

    public CountryListPlot getCountryListPlot(long countryListId, long days);

    public CountryListPlot getCountryListPlotOnTheFly(CountryList countryList);

}
