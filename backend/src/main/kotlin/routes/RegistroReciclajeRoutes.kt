package com.ecosuma.routes

import com.ecosuma.data.RegistroReciclajeRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import com.ecosuma.models.CrearRegistroReciclajeRequest
import com.ecosuma.models.RegistroReciclaje
import com.ecosuma.data.MaterialRepository
import com.ecosuma.data.CentroAcopioRepository
import io.ktor.server.request.receive
import io.ktor.server.routing.post
import java.time.LocalDateTime
import com.ecosuma.models.ConfirmarRegistroRequest
import io.ktor.server.routing.put

fun Route.registroReciclajeRoutes() {

    val repository = RegistroReciclajeRepository()
    val materialRepository = MaterialRepository()
    val centroRepository = CentroAcopioRepository()

    route("/api/reciclaje") {

        // Registrar una nueva solicitud de reciclaje
        post {
            val solicitud = try {
                call.receive<CrearRegistroReciclajeRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Datos de solicitud inválidos")
                )
                return@post
            }

            // Validar los datos recibidos
            if (
                solicitud.usuarioId <= 0 ||
                solicitud.centroAcopioId <= 0 ||
                solicitud.materialId <= 0 ||
                !solicitud.cantidadKg.isFinite() ||
                solicitud.cantidadKg <= 0.0
            ) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Los datos de la entrega no son válidos")
                )
                return@post
            }

            // Verificar que exista el material
            val material = materialRepository.buscarPorId(solicitud.materialId)

            if (material == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Material no encontrado")
                )
                return@post
            }

            // Verificar que exista el centro de acopio
            val centro = centroRepository.buscarPorId(solicitud.centroAcopioId)

            if (centro == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Centro de acopio no encontrado")
                )
                return@post
            }

            // Crear el registro pendiente
            val nuevoRegistro = RegistroReciclaje(
                id = 0,
                usuarioId = solicitud.usuarioId,
                centroAcopioId = solicitud.centroAcopioId,
                materialId = solicitud.materialId,
                cantidadKg = solicitud.cantidadKg,
                puntosObtenidos = 0,
                fecha = LocalDateTime.now().toString(),
                estado = "PENDIENTE",
                registradoPor = solicitud.usuarioId,
                confirmadoPor = null
            )

            // Guardar el registro
            val registroGuardado = repository.guardar(nuevoRegistro)

            call.respond(
                HttpStatusCode.Created,
                registroGuardado
            )
        }
        // Confirmar una entrega de reciclaje
        put("/{id}/confirmar") {

            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null || id <= 0) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "ID de registro inválido")
                )
                return@put
            }

            // Recibir los datos de confirmación
            val solicitud = try {
                call.receive<ConfirmarRegistroRequest>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Datos de confirmación inválidos")
                )
                return@put
            }

            if (solicitud.confirmadoPor <= 0) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "El identificador del personal no es válido")
                )
                return@put
            }

            // Buscar el registro
            val registro = repository.buscarPorId(id)

            if (registro == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Registro no encontrado")
                )
                return@put
            }

            // Verificar que esté pendiente
            if (registro.estado != "PENDIENTE") {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "La entrega ya fue procesada")
                )
                return@put
            }

            // Buscar el material para calcular los puntos
            val material = materialRepository.buscarPorId(registro.materialId)

            if (material == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Material no encontrado")
                )
                return@put
            }

            // Calcular los EcoPuntos
            val puntosCalculados =
                registro.cantidadKg * material.puntosPorKg

            // Validar que el resultado pueda almacenarse como Int
            if (!puntosCalculados.isFinite() ||
                puntosCalculados < 0 ||
                puntosCalculados > Int.MAX_VALUE) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "No es posible calcular los EcoPuntos")
                )
                return@put
            }

            val puntos = puntosCalculados.toInt()

            // Confirmar la entrega
            val registroConfirmado = repository.confirmar(
                id = id,
                confirmadoPor = solicitud.confirmadoPor,
                puntos = puntos
            )

            if (registroConfirmado == null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "La entrega no pudo confirmarse")
                )
                return@put
            }

            call.respond(
                HttpStatusCode.OK,
                registroConfirmado
            )
        }

        // Rechazar una entrega de reciclaje
        put("/{id}/rechazar") {

            // Obtener el ID de la entrega
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null || id <= 0) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "ID de registro inválido")
                )
                return@put
            }

            // Buscar la entrega
            val registro = repository.buscarPorId(id)

            if (registro == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Registro no encontrado")
                )
                return@put
            }

            // Verificar que la entrega esté pendiente
            if (registro.estado != "PENDIENTE") {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "La entrega ya fue procesada")
                )
                return@put
            }

            // Rechazar la entrega
            val registroRechazado = repository.rechazar(id)

            if (registroRechazado == null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "La entrega no pudo rechazarse")
                )
                return@put
            }

            // Responder con la entrega rechazada
            call.respond(
                HttpStatusCode.OK,
                registroRechazado
            )
        }

        // Buscar un registro por ID
        get("/{id}") {

            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "El ID debe ser un número válido")
                )
                return@get
            }

            val registro = repository.buscarPorId(id)

            if (registro == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Registro no encontrado")
                )
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                registro
            )
        }
    }
}