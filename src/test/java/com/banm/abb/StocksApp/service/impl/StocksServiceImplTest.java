package com.banm.abb.StocksApp.service.impl;

import com.banm.abb.StocksApp.dto.StocksAvailableDto;
import com.banm.abb.StocksApp.model.StocksAvailable;
import com.banm.abb.StocksApp.repository.StocksAvailableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StocksServiceImplTest {
    @InjectMocks
    private StocksServiceImpl service;

    @Mock
    private StocksAvailableRepository stocksAvailableRepository;

    @Test
    void listAllStocks() {
        var s1 = new StocksAvailable();
        var s2 = new StocksAvailable();

        s1.setId(0L);
        s1.setName("stock1");
        s1.setPrice(BigDecimal.ONE);
        s1.setAvailableCount(1000);

        s2.setId(1L);
        s2.setName("stock2");
        s2.setPrice(BigDecimal.ONE);
        s2.setAvailableCount(1000);

        List<StocksAvailableDto> list1 = List.of(StocksAvailableDto.builder()
                        .name(s1.getName())
                        .price(s1.getPrice())
                        .availableCount(s1.getAvailableCount()).build(),
                StocksAvailableDto.builder()
                        .name(s2.getName())
                        .price(s2.getPrice())
                        .availableCount(s2.getAvailableCount()).build());

        when(stocksAvailableRepository.findAll()).thenReturn(List.of(s1, s2));

        assertEquals(service.listAllStocks(), list1);
    }
}