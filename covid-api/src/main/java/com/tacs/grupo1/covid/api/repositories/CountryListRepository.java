package com.tacs.grupo1.covid.api.repositories;

import com.tacs.grupo1.covid.api.domain.CountryListEntity;
//import com.tacs.grupo1.covid.api.dto.CountryListsStats;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CountryListRepository extends PagingAndSortingRepository<CountryListEntity, Long> {

    public Optional<CountryListEntity> getById(long id);

    Page<CountryListEntity> findByUserId(Long userId, Pageable pageable);

    List<CountryListEntity> findByUserId(Long userId);

    List<CountryListEntity> findByCreationDateBetween(Date iniFecha, Date finFecha);

    List<CountryListEntity> findAllByIdIsNotNull();

    // Recupera si existe el nombre de una lista de paises por id de usuario y un nombre de lista
    // y a que además no tengan el mismo id de lista
    // Return nombre de la lista encontrada, si no encuentra nada devuelve null
    @Query("SELECT l.name FROM CountryListEntity l JOIN l.user u WHERE u.id=:idUser AND l.id<>:idList AND upper(l.name)=upper(:nameList)")
    String getJoinCountryListByUserIdAndListIdAndName(@Param("idUser")long id, @Param("idList")long idList, @Param("nameList")String nameList);

    // todo: query para obtener las stats
    //CountryListsStats getCountryListsStats();
}
