package com.ecosuma.data

import com.ecosuma.models.CentroAcopio

class CentroAcopioRepository {

    // Centros de acopio temporales para pruebas
    private val centros = listOf(
        CentroAcopio(
            id = 1,
            nombre = "Centro de Acopio EcoSuma",
            direccion = "Tulancingo, Hidalgo",
            latitud = 20.0836,
            longitud = -98.3629,
            horario = "Lunes a viernes de 9:00 a 17:00",
            telefono = "7750000000"
        ),
        CentroAcopio(
            id = 2,
            nombre = "Punto Verde EcoSuma",
            direccion = "Pachuca, Hidalgo",
            latitud = 20.1011,
            longitud = -98.7591,
            horario = "Lunes a sábado de 8:00 a 16:00",
            telefono = "7710000000"
        )
    )

    // Obtener todos los centros activos
    fun obtenerTodos(): List<CentroAcopio> {
        return centros.filter { it.activo }
    }

    // Buscar un centro por su ID
    fun buscarPorId(id: Int): CentroAcopio? {
        return centros.find { it.id == id && it.activo }
    }
}