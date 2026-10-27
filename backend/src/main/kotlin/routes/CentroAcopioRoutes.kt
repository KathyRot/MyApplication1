package com.ecosuma.routes

import com.ecosuma.data.CentroAcopioRepository
import com.ecosuma.models.CentroCercanoResponse
import com.ecosuma.utils.GeoUtils
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlin.math.round

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

        // Consultar centros de acopio cercanos
        get("/cercanos") {

            // Recibir coordenadas del ciudadano
            val latitud = call.request.queryParameters["latitud"]?.toDoubleOrNull()
            val longitud = call.request.queryParameters["longitud"]?.toDoubleOrNull()

            // Validar que las coordenadas sean correctas
            if (
                latitud == null ||
                longitud == null ||
                !latitud.isFinite() ||
                !longitud.isFinite() ||
                latitud !in -90.0..90.0 ||
                longitud !in -180.0..180.0
            ) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Debes proporcionar coordenadas válidas")
                )
                return@get
            }

            // Obtener centros activos y calcular distancias
            val centrosCercanos = repository.obtenerTodos().map { centro ->

                val distancia = GeoUtils.calcularDistancia(
                    latitud,
                    longitud,
                    centro.latitud,
                    centro.longitud
                )

                CentroCercanoResponse(
                    id = centro.id,
                    nombre = centro.nombre,
                    direccion = centro.direccion,
                    latitud = centro.latitud,
                    longitud = centro.longitud,
                    horario = centro.horario,
                    telefono = centro.telefono,
                    distanciaKm = round(distancia * 100.0) / 100.0
                )

            }.sortedBy { it.distanciaKm }

            // Devolver centros ordenados por cercanía
            call.respond(
                HttpStatusCode.OK,
                centrosCercanos
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