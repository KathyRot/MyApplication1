package com.ecosuma

import com.ecosuma.data.UserRepository
import com.ecosuma.models.LoginRequest
import com.ecosuma.models.LoginResponse
import com.ecosuma.models.toDto
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
    // Lee users.json una sola vez, al arrancar el servidor
    val userRepository = UserRepository()

    routing {
        // Para comprobar en el navegador que el servidor está encendido
        get("/") {
            call.respondText("EcoSuma API activa")
        }

        route("/api/auth") {
            // POST /api/auth/login   cuerpo: { "username": "...", "password": "..." }
            post("/login") {
                // ContentNegotiation convierte el JSON recibido en LoginRequest
                val request = call.receive<LoginRequest>()

                // 1. Campos vacíos -> 400
                if (request.username.isBlank() || request.password.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        LoginResponse(success = false, message = "Escribe tu usuario y contraseña")
                    )
                    return@post
                }

                // 2. Buscar en users.json
                val user = userRepository.authenticate(request.username, request.password)

                if (user == null) {
                    // 3. No coincide -> 401
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        LoginResponse(success = false, message = "Usuario o contraseña incorrectos")
                    )
                } else {
                    // 4. Correcto -> 200 con los datos del usuario (sin contraseña) y su rol
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