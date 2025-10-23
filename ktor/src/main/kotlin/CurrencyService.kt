package com.example

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object CurrencyService {

    fun loadAndSaveRates(rates: Map<String, CbrValuteItem>) {
        transaction {
            Currencies.deleteWhere { Op.TRUE }

            rates.values.forEach { item ->
                CurrencyEntity.new {
                    charCode = item.CharCode
                    name = item.Name
                    nominal = item.Nominal
                    value = item.Value
                }
            }
        }
    }

    fun getAllCurrencies(): List<CurrencyDto> = transaction {
        CurrencyEntity.all().map { it.toDto() }
    }

    fun getCurrencyById(id: Int): CurrencyDto? = transaction {
        CurrencyEntity.findById(id)?.toDto()
    }

    private fun CurrencyEntity.toDto() = CurrencyDto(
        id = id.value,
        charCode = charCode,
        name = name,
        nominal = nominal,
        value = value
    )
}