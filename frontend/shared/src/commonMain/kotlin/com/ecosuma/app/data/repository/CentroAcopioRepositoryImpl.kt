
package com.ecosuma.app.data.repository

import com.ecosuma.app.data.model.CentroAcopioDto
import com.ecosuma.app.data.model.CentroCercanoDto
import com.ecosuma.app.data.remote.CentroAcopioApi

/**
 * Repositorio para consultar centros de acopio
 * desde el frontend de EcoSuma.
 */
class CentroAcopioRepositoryImpl(
    private val centroAcopioApi: CentroAcopioApi
) {

    /**
     * Obtener todos los centros de acopio activos.
     */
    suspend fun obtenerCentros(): List<CentroAcopioDto> {
        return centroAcopioApi.obtenerCentros()
    }

    /**
     * Obtener un centro de acopio por su ID.
     */
    suspend fun obtenerCentroPorId(id: Int): CentroAcopioDto {
        return centroAcopioApi.obtenerCentroPorId(id)
    }

    /**
     * Obtener centros cercanos a la ubicación del ciudadano.
     *
     * El backend calcula las distancias y devuelve
     * los centros ordenados del más cercano al más lejano.
     */
    suspend fun obtenerCentrosCercanos(
        latitud: Double,
        longitud: Double
    ): List<CentroCercanoDto> {
        return centroAcopioApi.obtenerCentrosCercanos(
            latitud = latitud,
            longitud = longitud
        )
    }
}
