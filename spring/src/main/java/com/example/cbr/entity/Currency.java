package com.example.cbr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "char_code", nullable = false, unique = true)
    private String charCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nominal", nullable = false)
    private Integer nominal;

    @Column(name = "value", nullable = false)
    private Double value;


    public Currency(String charCode, String name, Integer nominal, Double value) {
        this.charCode = charCode;
        this.name = name;
        this.nominal = nominal;
        this.value = value;
    }
}
