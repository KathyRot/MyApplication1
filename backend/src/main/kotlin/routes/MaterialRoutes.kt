package com.ecosuma.routes

import com.ecosuma.data.MaterialRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.materialRoutes() {

    val repository = MaterialRepository()

    route("/api/materiales") {

        // Consultar todos los materiales disponibles
        get {
            call.respond(
                HttpStatusCode.OK,
                repository.obtenerTodos()
            )
        }

        // Consultar un material por su ID
        get("/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "El ID debe ser un número válido")
                )
                return@get
            }

            val material = repository.buscarPorId(id)

            if (material == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Material no encontrado")
                )
                return@get
            }

            call.respond(HttpStatusCode.OK, material)
        }
    }
}