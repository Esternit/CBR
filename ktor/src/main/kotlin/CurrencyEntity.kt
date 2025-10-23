package com.example

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Currencies : IntIdTable() {
    val charCode = varchar("char_code", 3).uniqueIndex()
    val name = varchar("name", 128)
    val nominal = integer("nominal")
    val value = double("value")
}

class CurrencyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CurrencyEntity>(Currencies)

    var charCode by Currencies.charCode
    var name by Currencies.name
    var nominal by Currencies.nominal
    var value by Currencies.value
}

@Serializable
data class CurrencyDto(
    val id: Int,
    val charCode: String,
    val name: String,
    val nominal: Int,
    val value: Double
)