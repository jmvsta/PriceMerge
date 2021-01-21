package com.jmvsta.pricemerge.utils;

import com.jmvsta.pricemerge.entity.Price;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;

public class PriceUtils {

    public enum PriceIntervalPosition {
        LEFT_OUTSIDE,
        RIGHT_OUTSIDE,
        COVERS,
        INCLUDED,
        LESS_EQUAL,
        MORE_EQUAL,
        EQUAL_MORE,
        EQUAL_LESS,
        LESS,
        MORE;

        public static PriceIntervalPosition getPositionByComparingIntervals(Price price1, Price price2) {
            Date begin1 = price1.getBeginDate(),
                    begin2 = price2.getBeginDate(),
                    end1 = price1.getEndDate(),
                    end2 = price2.getEndDate();

            if (end2.before(begin1)) {
                return LEFT_OUTSIDE;
            }

            if (end1.before(begin2)) {
                return RIGHT_OUTSIDE;
            }

            if (begin1.after(begin2) && end1.before(end2)) {
                return COVERS;
            }

            if (begin1.before(begin2) && end1.after(end2)) {
                return INCLUDED;
            }

            if (begin2.before(begin1) && end1.equals(end2)) {
                return LESS_EQUAL;
            }

            if (begin2.after(begin1) && end1.equals(end2)) {
                return MORE_EQUAL;
            }

            if (begin2.equals(begin1) && end1.before(end2)) {
                return EQUAL_MORE;
            }

            if (begin2.equals(begin1) && end1.after(end2)) {
                return EQUAL_LESS;
            }

            if (begin1.after(begin2) && end1.after(end2)) {
                return LESS;
            }

            if (begin1.before(begin2) && end1.before(end2)) {
                return MORE;
            }

            throw new IllegalArgumentException("comparison error");
        }

    }

    public static List<Price> mergePricesGroups(List<Price> oldPrices, List<Price> newPrices) {
        Set<Price> result = new HashSet<>();

        for (Price oldPrice : oldPrices) {
            for (Price newPrice : newPrices) {
                result.addAll(mergePrices(oldPrice, newPrice));
            }
        }

        return new ArrayList<>(result);
    }

    @SneakyThrows
    public static List<Price> mergePrices(Price price1, Price price2) {
        Price.PriceBuilder priceBuilderWithDef = Price.builder()
                .productCode(price1.getProductCode())
                .number(price1.getNumber())
                .depart(price1.getDepart());

        return switch (PriceIntervalPosition.getPositionByComparingIntervals(price1, price2)) {
            case LEFT_OUTSIDE, RIGHT_OUTSIDE -> List.of(price1, price2);
            case COVERS, LESS_EQUAL, EQUAL_MORE -> List.of(price2);
            case INCLUDED -> price1.getValue() == price2.getValue() ? List.of(price1) : List.of(
                    priceBuilderWithDef
                            .beginDate(price1.getBeginDate())
                            .endDate(price2.getBeginDate())
                            .value(price1.getValue())
                            .build(),
                    priceBuilderWithDef
                            .beginDate(price2.getBeginDate())
                            .endDate(price2.getEndDate())
                            .value(price2.getValue())
                            .build(),
                    priceBuilderWithDef
                            .beginDate(price2.getEndDate())
                            .endDate(price1.getEndDate())
                            .value(price1.getValue())
                            .build()
            );
            case MORE_EQUAL -> price1.getValue() == price2.getValue() ? List.of(price1) : List.of(
                    priceBuilderWithDef
                            .beginDate(price1.getBeginDate())
                            .endDate(price2.getBeginDate())
                            .value(price1.getValue())
                            .build(), price2
            );
            case EQUAL_LESS -> price1.getValue() == price2.getValue() ? List.of(price1) : List.of(
                    price2, priceBuilderWithDef
                            .beginDate(price2.getEndDate())
                            .endDate(price1.getEndDate())
                            .value(price1.getValue())
                            .build()
            );
            case LESS -> price1.getValue() == price2.getValue() ?
                    List.of(priceBuilderWithDef
                                    .beginDate(price2.getBeginDate())
                                    .endDate(price1.getEndDate())
                                    .value(price1.getValue())
                                    .build())
                    : List.of(priceBuilderWithDef
                            .beginDate(price2.getBeginDate())
                            .endDate(price1.getBeginDate())
                            .value(price2.getValue())
                            .build(),
                    priceBuilderWithDef
                            .beginDate(price2.getEndDate())
                            .endDate(price1.getEndDate())
                            .value(price1.getValue())
                            .build());
            case MORE -> price1.getValue() == price2.getValue() ?
                    List.of(priceBuilderWithDef
                            .beginDate(price1.getBeginDate())
                            .endDate(price2.getEndDate())
                            .value(price1.getValue())
                            .build())
                    : List.of(priceBuilderWithDef
                            .beginDate(price1.getBeginDate())
                            .endDate(price2.getBeginDate())
                            .value(price1.getValue())
                            .build(), price2);
        };
    }

//    public static List<Price> crossIntervals(Price p1, Price p2) {
//        Date begin1 = p1.getBeginDate(), begin2 = p2.getBeginDate(), end1 = p1.getEndDate(), end2 = p2.getEndDate();
//
////        same price crossing and not
//        if (p1.getValue() == p2.getValue()) {
//            return end1.before(begin2) ? List.of(p1, p2)
//                    : List.of(Price.builder()
//                    .productCode(p1.getProductCode())
//                    .number(p1.getNumber())
//                    .depart(p1.getDepart())
//                    .beginDate(begin1)
//                    .endDate(end2)
//                    .value(p1.getValue())
//                    .build());
//        }
//
//        if (begin1.equals(begin2)) {
//            return end1.equals(end2) ? List.of(p2) :
//
//                    List.of(p2, Price.builder()
//                            .productCode(p1.getProductCode())
//                            .number(p1.getNumber())
//                            .depart(p1.getDepart())
//                            .beginDate(end1)
//                            .endDate(end2)
//                            .value(p1.getValue())
//                            .build()
//                    : p1, p2));
//        }
//        //            summing (with equal value and not)
//        else if (end1.equals(begin2)) {
//            return List.of(p1, p2);
//        }
////            not crossing
//        else if (begin2.after(end1)) {
//            return List.of(p1, p2);
//        }
////            |b1           |e1
////              |b2     |e2
//        else if (begin2.before(end1) && end2.before(end1)) {
//            return List.of(Price.builder()
//                    .productCode(p1.getProductCode())
//                    .number(p1.getNumber())
//                    .depart(p1.getDepart())
//                    .beginDate(begin1)
//                    .endDate(begin2)
//                    .value(p1.getValue())
//                    .build(), p2, Price.builder()
//                    .productCode(p1.getProductCode())
//                    .number(p1.getNumber())
//                    .depart(p1.getDepart())
//                    .beginDate(end2)
//                    .endDate(end1)
//                    .value(p1.getValue())
//                    .build());
//        } else if (begin2.before(end1) && end2.after(end1)) {
//            return List.of(Price.builder()
//                    .productCode(p1.getProductCode())
//                    .number(p1.getNumber())
//                    .depart(p1.getDepart())
//                    .beginDate(begin1)
//                    .endDate(begin2)
//                    .value(p1.getValue())
//                    .build(), p2);
//        } else {
//            return null;
//        }
//    }

}
