package com.ecosuma

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

/**
 * Conecta kotlinx.serialization con Ktor:
 * convierte automáticamente JSON <-> data classes con @Serializable.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true        // respuestas con formato legible
            ignoreUnknownKeys = true  // si la app manda campos extra, no falla
        })
    }
}