package com.banm.abb.StocksApp.client;

import com.banm.abb.StocksApp.dto.StocksAvailableDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "http://localhost:8080/stocks", name = "feign-test")
public interface FeignTestClient {

    @GetMapping("/market/list-all")
    List<StocksAvailableDto> getStocks();
}
