package com.currency.app.currexchange.controller;

import com.currency.app.currexchange.dto.CurrRateDetails;
import com.currency.app.currexchange.service.ExRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ecb/")
public class ExRateAPI {

    @Autowired
    private ExRateService service;

    @GetMapping("getrate/{curr}/{baseCurr}")
    public ResponseEntity<String> getExchangeRate(@PathVariable String curr, @PathVariable String baseCurr) {
        return service.getExchangeRate(curr.trim().toUpperCase(), baseCurr.trim().toUpperCase());
    }

    @GetMapping("getlist")
    public ResponseEntity<List<CurrRateDetails>> getList(){
        return service.getCurrencyList();
    }

    @GetMapping("convert/{quantity}/{fromCurr}/{toCurr}")
    public ResponseEntity<Double> convertCurrency (@PathVariable double quantity, @PathVariable String fromCurr, @PathVariable String toCurr){
        return service.convertCurrency(fromCurr.trim().toUpperCase(), quantity, toCurr.trim().toUpperCase());
    }

}
