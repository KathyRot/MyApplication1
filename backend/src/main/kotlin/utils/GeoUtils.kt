package com.ecosuma.utils

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object GeoUtils {

    // Radio aproximado de la Tierra en kilómetros
    private const val RADIO_TIERRA = 6371.0

    // Calcular distancia entre dos ubicaciones
    fun calcularDistancia(
        latitud1: Double,
        longitud1: Double,
        latitud2: Double,
        longitud2: Double
    ): Double {

        // Convertir grados a radianes
        val lat1 = Math.toRadians(latitud1)
        val lon1 = Math.toRadians(longitud1)
        val lat2 = Math.toRadians(latitud2)
        val lon2 = Math.toRadians(longitud2)

        // Calcular diferencias
        val diferenciaLatitud = lat2 - lat1
        val diferenciaLongitud = lon2 - lon1

        // Fórmula de Haversine
        val a = sin(diferenciaLatitud / 2).pow(2.0) +
                cos(lat1) * cos(lat2) *
                sin(diferenciaLongitud / 2).pow(2.0)

        val c = 2 * asin(sqrt(a.coerceIn(0.0, 1.0)))

        // Devolver distancia en kilómetros
        return RADIO_TIERRA * c
    }
}