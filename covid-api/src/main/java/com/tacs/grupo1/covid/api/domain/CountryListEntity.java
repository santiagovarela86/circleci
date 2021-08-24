package com.tacs.grupo1.covid.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CountryListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "country_country_list", joinColumns = @JoinColumn(name = "country_list_id"), inverseJoinColumns = @JoinColumn(name = "country_id"))
    private Set<CountryEntity> countries = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, unique = false)
    private Date creationDate;
}
