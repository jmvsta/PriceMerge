package com.jmvsta.pricemerge.service;

import com.jmvsta.pricemerge.entity.Price;
import com.jmvsta.pricemerge.repository.PriceRepository;
import com.jmvsta.pricemerge.utils.PriceUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PriceService {

    private final PriceRepository priceRepository;

    @SneakyThrows
    public Price findPrice(long id) {
        return priceRepository.findById(id).get();
    }

    public List<Price> findAllPrices() {
        return priceRepository.findAll();
    }

    public List<Price> addAllPrices(List<Price> prices) {
        return priceRepository.saveAll(prices);
    }

    public Price addPrice(Price price) {
        return priceRepository.save(price);
    }

    public List<Price> findCurrentPricesAndMergeWithNew(List<Price> newPrices) {
        List<Price> currentPrices = findAllPrices();
        List<Price> resultPrices = mergePrices(currentPrices, newPrices);
        priceRepository.deleteInBatch(currentPrices);
        return resultPrices;
    }

    @SneakyThrows
    private List<Price> mergePrices(List<Price> currentPrices, List<Price> newPrices) {

//      divide old prices in categories of productCode, then number, then depart
        Map<String, Map<Integer, Map<Integer, List<Price>>>> groupedPricesByProductCodeAndNumberAndDepart =
                currentPrices.stream()
                .collect(Collectors.groupingBy(Price::getProductCode,
                        Collectors.groupingBy(Price::getNumber,
                                Collectors.groupingBy(Price::getDepart))));

        Set<Price> mergedPrices = new HashSet<>();

//      searching for new intervals of appropriate price (productCode, then number, then depart) and making lists of them
        groupedPricesByProductCodeAndNumberAndDepart.forEach((productCode, groupedPricesByProductCode) ->
                groupedPricesByProductCode.forEach((number, groupedPricesByNumber) ->
                        groupedPricesByNumber.forEach((depart, groupedPricesByDepart) -> {
                            List<Price> newGroupedPrices = newPrices.stream()
                                    .filter(np -> np.getProductCode().equals(productCode) && np.getNumber() == number
                                            && np.getDepart() == depart).collect(Collectors.toCollection(ArrayList::new));
                            mergedPrices.addAll(PriceUtils.mergePricesGroups(groupedPricesByDepart, newGroupedPrices));
                        })
                )
        );
        return priceRepository.saveAll(mergedPrices);
    }
}
