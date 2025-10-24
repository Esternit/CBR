package com.example.cbr.utils;

import com.example.cbr.dto.CbrResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class CbrClient {

    private final RestTemplate restTemplate;

    public CbrClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CbrResponse fetchRates() {
        String url = "https://www.cbr-xml-daily.ru/daily_json.js";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "CurrencyApp/1.0");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<CbrResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                CbrResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("CBR request failed: " + response.getStatusCode());
        }

        CbrResponse body = response.getBody();

        if (body == null || body.getValute() == null) {
            throw new RuntimeException("Empty or invalid CBR response");
        }

        return body;
    }
}