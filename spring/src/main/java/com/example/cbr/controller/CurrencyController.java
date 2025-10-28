package com.example.cbr.controller;

import com.example.cbr.entity.Currency;
import com.example.cbr.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getByCharCode(@PathVariable Integer id) {
        Currency currency = currencyService.getCurrencyByCharCode(id);
        if (currency != null) {
            return ResponseEntity.ok(currency);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}