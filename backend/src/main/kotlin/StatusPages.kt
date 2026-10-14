package com.ecosuma

import com.ecosuma.models.LoginResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

/**
 * Manejo de errores: en lugar de mostrar el error técnico,
 * se responde un JSON con el mismo formato que el login.
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        // La app mandó un JSON mal formado o incompleto
        exception<BadRequestException> { call, _ ->
            call.respond(
                HttpStatusCode.BadRequest,
                LoginResponse(success = false, message = "El cuerpo de la petición no es válido")
            )
        }
        // Cualquier otro error inesperado
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Error no controlado", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                LoginResponse(success = false, message = "Error interno del servidor")
            )
        }
    }
}