package com.nelolik.base_shop.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/account")
public class AccountController {

    @GetMapping("/account")
    public List<Object> getListOfOrders() {
        throw new UnsupportedOperationException("Not implemented yet");//TODO
    }

    public List<Object> getUnfinishedOrders() {
        throw new UnsupportedOperationException("Not implemented yet");//TODO
    }

    public Integer getBonusesCount() {
        throw new UnsupportedOperationException("Not implemented yet");//TODO
    }


}
