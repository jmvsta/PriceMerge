package com.jmvsta.pricemerge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@EntityScan({"com.jmvsta.pricemerge.entity"})
@SpringBootApplication(scanBasePackages = {"com.jmvsta.pricemerge"})
public class PriceMergeApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(PriceMergeApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PriceMergeApplication.class, args);
    }


}
