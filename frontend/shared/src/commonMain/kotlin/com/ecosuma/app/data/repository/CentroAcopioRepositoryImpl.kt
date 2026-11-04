
package com.ecosuma.app.data.repository

import com.ecosuma.app.data.model.CentroAcopioDto
import com.ecosuma.app.data.remote.CentroAcopioApi

/**
 * Repositorio del frontend para consultar
 * los centros de acopio de EcoSuma.
 */
class CentroAcopioRepositoryImpl(
    private val centroAcopioApi: CentroAcopioApi
) {

    /**
     * Obtiene todos los centros de acopio activos.
     */
    suspend fun obtenerCentros(): List<CentroAcopioDto> {
        return centroAcopioApi.obtenerCentros()
    }

    /**
     * Obtiene un centro de acopio por su ID.
     */
    suspend fun obtenerCentroPorId(id: Int): CentroAcopioDto {
        return centroAcopioApi.obtenerCentroPorId(id)
    }
}
