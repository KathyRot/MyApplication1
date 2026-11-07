
package com.ecosuma.app.data.remote

import com.ecosuma.app.BASE_URL
import com.ecosuma.app.data.model.CentroAcopioDto
import com.ecosuma.app.data.model.CentroCercanoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Comunicación con el backend de EcoSuma
 * para consultar centros de acopio.
 */
class CentroAcopioApi(
    private val client: HttpClient
) {

    /**
     * Consultar todos los centros activos.
     * GET /api/centros-acopio
     */
    suspend fun obtenerCentros(): List<CentroAcopioDto> {
        return client.get(
            "$BASE_URL/api/centros-acopio"
        ).body()
    }

    /**
     * Consultar un centro por ID.
     * GET /api/centros-acopio/{id}
     */
    suspend fun obtenerCentroPorId(id: Int): CentroAcopioDto {
        return client.get(
            "$BASE_URL/api/centros-acopio/$id"
        ).body()
    }

    /**
     * Consultar centros cercanos al ciudadano.
     * El backend calcula la distancia en kilómetros
     * y devuelve los centros ordenados por cercanía.
     */
    suspend fun obtenerCentrosCercanos(
        latitud: Double,
        longitud: Double
    ): List<CentroCercanoDto> {

        return client.get(
            "$BASE_URL/api/centros-acopio/cercanos"
        ) {
            parameter("latitud", latitud)
            parameter("longitud", longitud)
        }.body()
    }
}
