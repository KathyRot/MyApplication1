package com.ecosuma

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.Level

/** Muestra en la consola cada petición que llega (método, ruta y resultado). */
fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
    }
}