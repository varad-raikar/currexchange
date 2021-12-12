package com.currency.app.currexchange.service;

import com.currency.app.currexchange.dto.CurrRateDetails;
import com.currency.app.currexchange.exception.CSVFileException;
import com.currency.app.currexchange.exception.InvalidInputCurrencyException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ServiceImpl implements ExRateService{

    Map<String, CurrRateDetails> map = new HashMap<>();
    
    @PostConstruct
    public void init() throws CsvValidationException, IOException, URISyntaxException {
        List<List<String>> list = new ArrayList<>();
        String[] data;
        int rowCount = 0;

        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource("eurofxref.csv").toURI()));
        CSVReader csvReader = new CSVReader(reader);

        while ((data = csvReader.readNext()) != null) {
            List<String> itemList = new ArrayList<String>();
            rowCount++;

            if (rowCount > 2) {
                throw new CSVFileException("File contains more than 2 lines");
            }

            for(int index=0; index < data.length; index++ ) {
                if (index > 0) {
                    itemList.add(data[index].trim());
                }
            }
            list.add(itemList);
        }

        Iterator currencyIter = list.get(0).iterator();
        Iterator rateIter = list.get(1).iterator();

        while (currencyIter.hasNext() && rateIter.hasNext()){
            String currency = (String) currencyIter.next();
            String rate = (String) rateIter.next();
            if (!currency.isEmpty() && !rate.isEmpty() && !currency.isBlank() && !rate.isBlank()) {
                map.put(currency, new CurrRateDetails(currency, Double.parseDouble(rate)));
            }
        }

        reader.close();
        csvReader.close();     
    }

    @Override
    public ResponseEntity<String> getExchangeRate(String curr, String baseCurr) {
        if (validCurrency(curr) && validCurrency(baseCurr) && curr != "EUR") {
            CurrRateDetails currDetails = map.get(curr);
            currDetails.setRequested(currDetails.getRequested() + 1);

            if (baseCurr.equals("EUR")) {
                return new ResponseEntity<>(String.valueOf(currDetails.getRate()), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(String.valueOf((currDetails.getRate()/map.get(baseCurr).getRate())), HttpStatus.OK);
            }
        }else {
            throw new InvalidInputCurrencyException("Invalid input currency : " + curr + " " + baseCurr);
        }
    }

    @Override
    public ResponseEntity<List<CurrRateDetails>> getCurrencyList() {
        return new ResponseEntity<>(new ArrayList<>(map.values()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Double> convertCurrency(String fromCurr, double quantity, String toCurr) {
        double exchangeRate = Double.parseDouble(getExchangeRate(toCurr, fromCurr).getBody());
        return new ResponseEntity<>(exchangeRate*quantity, HttpStatus.OK);
    }

    @Override
    public boolean validCurrency(String curr) {
        if (map.containsKey(curr) || curr.equals("EUR")) {
            return true;
        }
        else {
            return false;
        }
    }
}
