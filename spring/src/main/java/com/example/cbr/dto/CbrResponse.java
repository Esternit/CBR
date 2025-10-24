package com.example.cbr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CbrResponse {
    @JsonProperty("Date")
    private String Date;
    @JsonProperty("PreviousDate")
    private String previousDate;
    @JsonProperty("Valute")
    private Map<String, ValuteInfo> Valute;
    @JsonProperty("PreviousURL")
    private String previousUrl;
    @JsonProperty("Timestamp")
    private String timestamp;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValuteInfo {
        @JsonProperty("ID")
        private String ID;
        @JsonProperty("NumCode")
        private String numCode;
        @JsonProperty("CharCode")
        private String charCode;
        @JsonProperty("Nominal")
        private Integer Nominal;
        @JsonProperty("Name")
        private String Name;
        @JsonProperty("Value")
        private Double Value;
        @JsonProperty("Previous")
        private Double Previous;
    }
}