package com.ecosuma

import com.ecosuma.data.UserRepository
import com.ecosuma.models.LoginRequest
import com.ecosuma.models.LoginResponse
import com.ecosuma.models.toDto

import com.ecosuma.routes.materialRoutes
import com.ecosuma.routes.centroAcopioRoutes
import com.ecosuma.routes.registroReciclajeRoutes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {

    // Leer los usuarios desde users.json
    val userRepository = UserRepository()

    routing {

        // Módulo de materiales reciclables
        materialRoutes()

        // Módulo de centros de acopio y geolocalización
        centroAcopioRoutes()

        // Módulo de registros de reciclaje
        registroReciclajeRoutes()

        // Comprobar que el servidor esté funcionando
        get("/") {
            call.respondText("EcoSuma API activa")
        }

        // Autenticación de usuarios
        route("/api/auth") {

            post("/login") {

                val request = call.receive<LoginRequest>()

                // Validar campos vacíos
                if (request.username.isBlank() || request.password.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        LoginResponse(
                            success = false,
                            message = "Escribe tu usuario y contraseña"
                        )
                    )
                    return@post
                }

                // Buscar usuario
                val user = userRepository.authenticate(
                    request.username,
                    request.password
                )

                if (user == null) {

                    call.respond(
                        HttpStatusCode.Unauthorized,
                        LoginResponse(
                            success = false,
                            message = "Usuario o contraseña incorrectos"
                        )
                    )

                } else {

                    call.respond(
                        HttpStatusCode.OK,
                        LoginResponse(
                            success = true,
                            message = "Bienvenido, ${user.fullName}",
                            token = "local-${user.id}-${System.currentTimeMillis()}",
                            user = user.toDto()
                        )
                    )
                }
            }
        }
    }
}