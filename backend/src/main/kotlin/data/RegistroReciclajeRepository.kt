package com.ecosuma.data

import com.ecosuma.models.RegistroReciclaje

class RegistroReciclajeRepository {

    private val registros = mutableListOf<RegistroReciclaje>()

    private var siguienteId = 1

    // Obtener todos los registros
    fun obtenerTodos(): List<RegistroReciclaje> {
        return registros.toList()
    }

    // Buscar un registro por su ID
    fun buscarPorId(id: Int): RegistroReciclaje? {
        return registros.find { it.id == id }
    }

    // Obtener registros de un ciudadano
    fun obtenerPorUsuario(usuarioId: Int): List<RegistroReciclaje> {
        return registros.filter { it.usuarioId == usuarioId }
    }

    // Obtener registros de un centro de acopio
    fun obtenerPorCentro(centroAcopioId: Int): List<RegistroReciclaje> {
        return registros.filter {
            it.centroAcopioId == centroAcopioId
        }
    }

    // Guardar un nuevo registro
    fun guardar(registro: RegistroReciclaje): RegistroReciclaje {

        val nuevoRegistro = registro.copy(
            id = siguienteId++
        )

        registros.add(nuevoRegistro)

        return nuevoRegistro
    }

    // Actualizar un registro existente
    fun actualizar(registro: RegistroReciclaje): Boolean {

        val indice = registros.indexOfFirst {
            it.id == registro.id
        }

        if (indice == -1) {
            return false
        }

        registros[indice] = registro

        return true
    }
    // Confirmar una entrega de reciclaje
    @Synchronized
    fun confirmar(
        id: Int,
        confirmadoPor: Int,
        puntos: Int
    ): RegistroReciclaje? {

        val registro = buscarPorId(id) ?: return null

        // Evitar confirmar dos veces la misma entrega
        if (registro.estado != "PENDIENTE") {
            return null
        }

        val registroConfirmado = registro.copy(
            estado = "CONFIRMADO",
            puntosObtenidos = puntos,
            confirmadoPor = confirmadoPor
        )

        actualizar(registroConfirmado)

        return registroConfirmado
    }

    // Rechazar una entrega de reciclaje
    @Synchronized
    fun rechazar(id: Int): RegistroReciclaje? {

        val registro = buscarPorId(id) ?: return null

        // Solo se pueden rechazar entregas pendientes
        if (registro.estado != "PENDIENTE") {
            return null
        }

        val registroRechazado = registro.copy(
            estado = "RECHAZADO",
            puntosObtenidos = 0,
            confirmadoPor = null
        )

        actualizar(registroRechazado)

        return registroRechazado
    }
}