package com.ecosuma

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

/**
 * CORS: permite que la app (sobre todo la versión de escritorio o web)
 * haga peticiones a este servidor desde otro origen.
 */
fun Application.configureHttp() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)            // el login es un POST
        allowHeader(HttpHeaders.ContentType)    // la app manda "application/json"
        allowHeader(HttpHeaders.Authorization)
        anyHost() // Solo para desarrollo local. En producción se limita a dominios conocidos.
    }
}
