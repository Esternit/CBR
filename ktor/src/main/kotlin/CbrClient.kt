package com.example

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CbrApiResponse(
    val Valute: Map<String, CbrValuteItem>,
    val PreviousURL: String,
    val Timestamp: String,
    val Date: String,
    val PreviousDate: String,
)

@Serializable
data class CbrValuteItem(
    val ID: String,
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Double,
    val Previous: Double
)

object CbrClient {
    private val client = HttpClient(CIO)

    suspend fun fetchRates(): Map<String, CbrValuteItem> {
        val text = client
            .get("https://www.cbr-xml-daily.ru/daily_json.js")
            .bodyAsText()
        val response = Json.decodeFromString<CbrApiResponse>(text)
        return response.Valute
    }
}