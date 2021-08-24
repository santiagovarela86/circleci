package com.tacs.grupo1.covid.api.services;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.dto.CountryListsStats;

import java.util.List;
import java.util.Optional;

public interface CountryListService {

    CountryListEntity get(long id);

    Optional<CountryListEntity> getOptional(long id);

    CountryListEntity save(CountryListEntity countryList);

    CountryListEntity update(long id, CountryListEntity countryList);

    List<CountryListEntity> getList(Integer pageNumber, Integer pageSize, String sortBy);

    List<CountryListEntity> getListByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy);

    CountryListEntity addCountry(long listId, long countryid);

    void delete(long id);

    List<CountryEntity> compare(long id1, long id2);

    CountryListsStats getInfo(long d);

    String findCountryListNameByUserIdAndListName(long userId, String listName, long listId);
}
