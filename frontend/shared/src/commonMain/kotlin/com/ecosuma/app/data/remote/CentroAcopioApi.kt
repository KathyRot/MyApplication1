
package com.ecosuma.app.data.remote

import com.ecosuma.app.BASE_URL
import com.ecosuma.app.data.model.CentroAcopioDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Comunicación con el backend de EcoSuma
 * para consultar centros de acopio.
 */
class CentroAcopioApi(
    private val client: HttpClient
) {

    /**
     * GET /api/centros-acopio
     * Consulta todos los centros activos.
     */
    suspend fun obtenerCentros(): List<CentroAcopioDto> {
        return client.get(
            "$BASE_URL/api/centros-acopio"
        ).body()
    }

    /**
     * GET /api/centros-acopio/{id}
     * Consulta un centro específico.
     */
    suspend fun obtenerCentroPorId(id: Int): CentroAcopioDto {
        return client.get(
            "$BASE_URL/api/centros-acopio/$id"
        ).body()
    }
}
