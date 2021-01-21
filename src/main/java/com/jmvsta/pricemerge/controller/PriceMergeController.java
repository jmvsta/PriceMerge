package com.jmvsta.pricemerge.controller;

import com.jmvsta.pricemerge.dto.PriceDto;
import com.jmvsta.pricemerge.entity.Price;
import com.jmvsta.pricemerge.service.PriceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import static com.jmvsta.pricemerge.utils.ConversionUtils.fromDto;
import static com.jmvsta.pricemerge.utils.ConversionUtils.toDto;

@Slf4j
@RestController
@RequestMapping(value="/prices")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PriceMergeController {

    private final PriceService priceService;

    @PostMapping(value="/add")
    public @ResponseBody PriceDto createPrices(@RequestBody PriceDto priceDto) {
        Price priceEntity = priceService.addPrice(fromDto(priceDto));
        return toDto(priceEntity);
    }

    @PostMapping(value="/addall")
    public @ResponseBody List<PriceDto> createAllPrices(@RequestBody List<PriceDto> priceDtos) {
        List<Price> priceEntities = fromDto(priceDtos);
        return toDto(priceService.addAllPrices(priceEntities));
    }

    @GetMapping(value = "/all")
    public @ResponseBody List<PriceDto> getAllPrices() {
        return toDto(priceService.findAllPrices());
    }

    @GetMapping
    public @ResponseBody PriceDto getPrice(@RequestParam long id) {
        return toDto(priceService.findPrice(id));
    }

//    @PutMapping(value="/")
//    public @ResponseBody List<Price> updatePrices() {
//        return null;
//    }

    @PutMapping(value="/merge")
    public @ResponseBody List<PriceDto> mergePrices(@RequestBody List<PriceDto> newPrices) {
        List<Price> priceEntities = fromDto(newPrices);
        return toDto(priceService.findCurrentPricesAndMergeWithNew(priceEntities));
    }
}
