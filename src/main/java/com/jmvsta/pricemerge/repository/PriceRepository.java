package com.jmvsta.pricemerge.repository;

import com.jmvsta.pricemerge.entity.Price;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
