package com.jmvsta.pricemerge.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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

@Entity(name = "price")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode @ToString @Builder
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRICE_SEQ")
    @SequenceGenerator(name="PRICE_SEQ", sequenceName="price_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude private long id;

    @Basic(optional = false)
    private String productCode;

    @Basic(optional = false)
    private int number;

    @Basic(optional = false)
    private int depart;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Basic(optional = false)
    private long value;

}