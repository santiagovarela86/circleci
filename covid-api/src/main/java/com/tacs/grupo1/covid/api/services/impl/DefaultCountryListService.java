package com.tacs.grupo1.covid.api.services.impl;

import com.google.common.collect.Sets;
import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.domain.CountryListEntity;
import com.tacs.grupo1.covid.api.dto.CountryListsStats;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.repositories.CountryListRepository;
import com.tacs.grupo1.covid.api.services.CountryListService;
import com.tacs.grupo1.covid.api.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("countryListService")
public class DefaultCountryListService implements CountryListService {

    private final CountryListRepository countryListRepository;
    private final CountryService countryService;

    @Autowired
    public DefaultCountryListService(CountryListRepository countryListRepository, CountryService countryService) {
        this.countryListRepository = countryListRepository;
        this.countryService = countryService;
    }

    public CountryListEntity get(long id) {
        return countryListRepository.getById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Optional<CountryListEntity> getOptional(long id) {
        return countryListRepository.getById(id);
    }

    @Override
    public CountryListEntity save(CountryListEntity countryList) {
        return countryListRepository.save(countryList);
    }

    @Override
    public CountryListEntity update(long id, CountryListEntity countryList) {
        CountryListEntity countryListEntity = get(id);

        countryListEntity.setCountries(countryList.getCountries());
        countryListEntity.setName(countryList.getName());
        
        return save(countryListEntity);
    }

    @Override
    public List<CountryListEntity> getList(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<CountryListEntity> pagedResult = countryListRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    public List<CountryListEntity> getListByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<CountryListEntity> pagedResult = countryListRepository.findByUserId(userId,paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    public CountryListEntity addCountry(long listId, long countryid) {
        CountryEntity country = countryService.get(countryid);
        CountryListEntity countryList = get(listId);
        countryList.getCountries().add(country);
        return save(countryList);
    }


    @Override
    public void delete(long id) {
        CountryListEntity countryListEntity = get(id);
        countryListRepository.delete(countryListEntity);
    }

    @Override
    public List<CountryEntity> compare(long firstId, long secondId) {
        Optional<CountryListEntity> countryList1 = countryListRepository.getById(firstId);
        Optional<CountryListEntity> countryList2 = countryListRepository.getById(secondId);

        if (countryList1.isEmpty() || countryList2.isEmpty()) {
            throw new NotFoundException();
        }
        return new ArrayList<>(Sets.intersection(countryList1.get().getCountries(), countryList2.get().getCountries()));
    }

    @Override
    public CountryListsStats getInfo(long d) {

        Date endDate=null;
        Date startDate=null;
        long count=0;

        if(d>=0){

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                endDate = dateFormat.parse(dateFormat.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            startDate = new Date(endDate.getTime() + TimeUnit.DAYS.toMillis(d * (-1)));

            count = countryListRepository.findByCreationDateBetween(startDate,endDate).size();

        }
        else{
            count = countryListRepository.findAllByIdIsNotNull().size();
        }

        return new CountryListsStats().setLastDays(d).setStartDate(startDate).setEndDate(endDate).setCount(count);
    }

    @Override
    public String findCountryListNameByUserIdAndListName(long userId, String listName, long listId) {
            return countryListRepository.getJoinCountryListByUserIdAndListIdAndName(userId,listId,listName);
    }

    private CountryListEntity mergeCountryListEntity(CountryListEntity actual, CountryListEntity countryList) {
        return actual.setName(countryList.getName())
                .setUser(countryList.getUser())
                .setCountries(countryList.getCountries());
    }
}
