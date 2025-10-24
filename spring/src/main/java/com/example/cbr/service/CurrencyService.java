package com.example.cbr.service;

import com.example.cbr.dto.CbrResponse;
import com.example.cbr.entity.Currency;
import com.example.cbr.repository.CurrencyRepository;
import com.example.cbr.utils.CbrClient;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CbrClient cbrClient;

    public CurrencyService(CurrencyRepository currencyRepository, CbrClient cbrClient) {
        this.currencyRepository = currencyRepository;
        this.cbrClient = cbrClient;
    }

    @PostConstruct
    @Transactional
    public void loadInitialRates() {
        CbrResponse response = cbrClient.fetchRates();
        List<Currency> currencies = response.getValute().values().stream()
                .map(info -> new Currency(
                        info.getCharCode(),
                        info.getName(),
                        info.getNominal(),
                        info.getValue()
                ))
                .collect(Collectors.toList());

        currencyRepository.deleteAll();
        currencyRepository.saveAll(currencies);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency getCurrencyByCharCode(Integer id) {
        return currencyRepository.findById(id)
                .orElse(null);
    }
}
