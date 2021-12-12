package com.currency.app.currexchange.exception;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ErrorResponse {
    private int Status;

}
