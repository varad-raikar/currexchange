package com.currency.app.currexchange.service;

import com.currency.app.currexchange.dto.CurrRateDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExRateService {
    ResponseEntity<String> getExchangeRate(String curr, String baseCurr);
    ResponseEntity<List<CurrRateDetails>> getCurrencyList();
    ResponseEntity<Double> convertCurrency(String fromCurr, double quantity, String toCurr );
    boolean validCurrency (String curr);
}
