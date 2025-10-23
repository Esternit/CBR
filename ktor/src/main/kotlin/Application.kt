package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    initDatabase()

    launch {
        loadInitialRates()
    }

    routing {
        get("/currencies") {
            val currencies = CurrencyService.getAllCurrencies()
            call.respond(currencies)
        }

        get("/currencies/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@get
            }
            val currency = CurrencyService.getCurrencyById(id)
            if (currency != null) {
                call.respond(currency)
            } else {
                call.respondText("Currency not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}
