package com.jmvsta.pricemerge;


import com.jmvsta.pricemerge.entity.Price;
import com.jmvsta.pricemerge.repository.PriceRepository;
import com.jmvsta.pricemerge.service.PriceService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class PriceServiceTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    private PriceService priceService;

    @Mock PriceRepository priceRepository;

    @SneakyThrows
    @BeforeEach
    void init() {
        List<Price> currentPrices = Arrays.asList(
                Price.builder()
                        .productCode("122856")
                        .number(1)
                        .depart(1)
                        .beginDate(formatter.parse("01.01.2013 00:00:00"))
                        .endDate(formatter.parse("31.01.2013 23:59:59"))
                        .value(11000L)
                        .build(),
                Price.builder()
                        .productCode("122856")
                        .number(2)
                        .depart(1)
                        .beginDate(formatter.parse("10.01.2013 00:00:00"))
                        .endDate(formatter.parse("20.01.2013 23:59:59"))
                        .value(92000L)
                        .build(),
                Price.builder()
                        .productCode("6654")
                        .number(1)
                        .depart(2)
                        .beginDate(formatter.parse("01.01.2013 00:00:00"))
                        .endDate(formatter.parse("31.01.2013 00:00:00"))
                        .value(4000L)
                        .build()
        );
        priceService = new PriceService(priceRepository);
        Mockito.lenient().when(priceRepository.findAll()).thenReturn(currentPrices);
    }

    @SneakyThrows
    @Test
    void mergePrices_ok() {
        // Given
        when(priceRepository.saveAll(any(List.class))).then(new Answer<List>() {

            long sequence = 4L;

            @Override
            public List answer(InvocationOnMock invocation) {
                List<Price> prices = invocation.getArgument(0);
                prices.forEach(price -> price.setId(sequence++));

                return prices;
            }
        });

        priceService = new PriceService(priceRepository);

        List<Price> newPrices = Arrays.asList(
                Price.builder()
                        .productCode("122856")
                        .number(1)
                        .depart(1)
                        .beginDate(formatter.parse("20.01.2013 00:00:00"))
                        .endDate(formatter.parse("20.02.2013 23:59:59"))
                        .value(11000L)
                        .build(),
                Price.builder()
                        .productCode("122856")
                        .number(2)
                        .depart(1)
                        .beginDate(formatter.parse("15.01.2013 00:00:00"))
                        .endDate(formatter.parse("25.01.2013 23:59:59"))
                        .value(92000L)
                        .build(),
                Price.builder()
                        .productCode("6654")
                        .number(1)
                        .depart(2)
                        .beginDate(formatter.parse("12.01.2013 00:00:00"))
                        .endDate(formatter.parse("13.01.2013 00:00:00"))
                        .value(4000L)
                        .build()
              );
        // When
        List<Price> mergedPrices = priceService.findCurrentPricesAndMergeWithNew(newPrices);


        // Then
        verify(priceRepository).saveAll(mergedPrices);
        mergedPrices.forEach(price -> Assertions.assertNotEquals(price.getId(), 0L));



//        verify(userRepository).insert(user);
//        Assertions.assertNotNull(user.getId());
//        verify(mailClient).sendUserRegistrationMail(insertedUser);
    }
}
