package com.example

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase() {
    val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/db"
    val dbUser = System.getenv("DB_USER") ?: "user"
    val dbPassword = System.getenv("DB_PASSWORD") ?: "pass"

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}

suspend fun loadInitialRates() {
    val rates = CbrClient.fetchRates()
    CurrencyService.loadAndSaveRates(rates)
}