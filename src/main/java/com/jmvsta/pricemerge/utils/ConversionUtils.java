package com.jmvsta.pricemerge.utils;

import com.jmvsta.pricemerge.dto.PriceDto;
import com.jmvsta.pricemerge.entity.Price;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ConversionUtils {

    public static <T, R> R merge(T first, T second, BiFunction<T, T, R> merger) {
        return merger == null ? null : merger.apply(first, second);
    }

    public static Price fromDto(PriceDto priceDto) {
        return Price.builder()
                .productCode(priceDto.getProductCode())
                .number(priceDto.getNumber())
                .depart(priceDto.getDepart())
                .beginDate(priceDto.getBeginDate())
                .endDate(priceDto.getEndDate())
                .value(priceDto.getValue())
                .build();
    }

    public static List<Price> fromDto(List<PriceDto> priceDtos) {
        return priceDtos.stream().map(ConversionUtils::fromDto).collect(Collectors.toCollection(ArrayList::new));
    }

    public static PriceDto toDto(Price price) {
        return PriceDto.builder()
                .id(price.getId())
                .productCode(price.getProductCode())
                .number(price.getNumber())
                .depart(price.getDepart())
                .beginDate(price.getBeginDate())
                .endDate(price.getEndDate())
                .value(price.getValue())
                .build();
    }

    public static List<PriceDto> toDto(List<Price> prices) {
        return prices.stream().map(ConversionUtils::toDto).collect(Collectors.toCollection(ArrayList::new));
    }


}
