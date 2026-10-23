package com.ecosuma.routes

import com.ecosuma.data.CentroAcopioRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.centroAcopioRoutes() {

    val repository = CentroAcopioRepository()

    route("/api/centros-acopio") {

        // Consultar todos los centros de acopio
        get {
            call.respond(
                HttpStatusCode.OK,
                repository.obtenerTodos()
            )
        }

        // Consultar un centro de acopio por ID
        get("/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "El ID debe ser un número válido")
                )
                return@get
            }

            val centro = repository.buscarPorId(id)

            if (centro == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Centro de acopio no encontrado")
                )
                return@get
            }

            call.respond(HttpStatusCode.OK, centro)
        }
    }
}