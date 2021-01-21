package com.jmvsta.pricemerge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jmvsta.pricemerge.entity.Price;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class PriceDto {

    private long id;

    private String productCode;

    private int number;

    private int depart;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss.SSS'Z'", timezone="Europe/Moscow")
    private Date beginDate;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss.SSS'Z'", timezone="Europe/Moscow")
    private Date endDate;

    private long value;

}