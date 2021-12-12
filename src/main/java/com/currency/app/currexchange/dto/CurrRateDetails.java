package com.currency.app.currexchange.dto;

import lombok.Data;

@Data
public class CurrRateDetails {
    private String currency;
    private double rate;
    private int requested;

    public CurrRateDetails(String currency, double rate) {
        this.currency = currency;
        this.rate = rate;
    }
}
