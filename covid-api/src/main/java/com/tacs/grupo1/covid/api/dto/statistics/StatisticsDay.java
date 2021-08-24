package com.tacs.grupo1.covid.api.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StatisticsDay {
    private OffsetDateTime date;
    protected Long confirmed;
    protected Long deaths;
    protected Long recovered;
}
