package com.ecosuma.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Crea el cliente HTTP de Ktor.
 * No se indica motor: ktor-client-engine-defaults elige el correcto
 * para cada plataforma (Android, escritorio, iOS).
 */
object HttpClientFactory {
    fun create(): HttpClient = HttpClient {
        // Conecta kotlinx.serialization: JSON <-> data classes @Serializable
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        // Muestra las peticiones en el Logcat
        install(Logging) { level = LogLevel.INFO }
        // Si el servidor no responde, se cancela después de 15 segundos
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
        }
    }
}