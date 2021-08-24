package com.tacs.grupo1.covid.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String isoCountryCode;

    @Column()
    private String strategy = "";

    @Column()
    private OffsetDateTime startDate;

    @Column(nullable = true, unique = false)
    private Long offsetVal;

    @ManyToMany(mappedBy = "countries")
    private Set<CountryListEntity> countryList = new HashSet<>();
}
