
package com.ecosuma

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

/**
 * Configuración CORS para EcoSuma Web.
 * Permite la comunicación entre el frontend
 * Kotlin/Wasm y el backend Ktor.
 */
fun Application.configureHttp() {

    install(CORS) {

        // Métodos HTTP permitidos
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)

        // Encabezados permitidos
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)

        // Dominio web configurado en Railway
        val webHost = System.getenv("ECOSUMA_WEB_HOST")
            ?.trim()
            ?.takeIf { it.isNotEmpty() }

        if (webHost != null) {

            // Entorno de producción
            allowHost(
                webHost,
                schemes = listOf("https")
            )

        } else {

            // Entorno de desarrollo local
            allowHost(
                "localhost",
                schemes = listOf("http")
            )

            allowHost(
                "localhost:8080",
                schemes = listOf("http")
            )

            allowHost(
                "localhost:8081",
                schemes = listOf("http")
            )

            allowHost(
                "localhost:3000",
                schemes = listOf("http")
            )
        }
    }
}

