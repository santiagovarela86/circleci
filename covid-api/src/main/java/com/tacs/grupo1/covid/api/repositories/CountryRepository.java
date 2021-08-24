package com.tacs.grupo1.covid.api.repositories;

import com.tacs.grupo1.covid.api.domain.CountryEntity;
import com.tacs.grupo1.covid.api.dto.QResponseCountries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<CountryEntity, Long> {

    public Optional<CountryEntity> getById(long id);

    // Todas las listas al que pertenece el país
    // (Si un usuario lo cargo es más de una lista, el usuario se repite)
    // Return user_name, list_name, country_name
    @Query("SELECT new com.tacs.grupo1.covid.api.dto.QResponseCountries(u.userName,l.name,p.name) FROM CountryEntity p JOIN p.countryList l JOIN l.user u WHERE p.id=:idCountry")
    public List<QResponseCountries> getJoinInformation(@Param("idCountry")long id);

    // Todas los usuarios que cargaron el país en alguna de sus listas
    // (No devuelve usuarios repetidos, pero devuelve una lista de strings)
    @Query("SELECT DISTINCT u.userName FROM CountryEntity p JOIN p.countryList l JOIN l.user u WHERE p.id=:idCountry")
    public List<String> getJoinCountryInterest(@Param("idCountry")long id);

    //  Recupera lista de paises ordenados por nombre y segmentados en páginas
    public Page<CountryEntity> findAllByNameStartingWith(String name, Pageable pageable);

    public Optional<CountryEntity> findByIsoCountryCode(String isoCountryCode);

    public Optional<List<CountryEntity>> findAllByStartDateNull();

    // todo: query
    // public long getTotalLists();
}
