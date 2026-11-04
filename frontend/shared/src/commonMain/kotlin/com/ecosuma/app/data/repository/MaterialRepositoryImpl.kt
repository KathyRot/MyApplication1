
package com.ecosuma.app.data.repository

import com.ecosuma.app.data.model.MaterialDto
import com.ecosuma.app.data.remote.MaterialApi

/**
 * Repositorio de materiales reciclables.
 *
 * Obtiene la información desde el backend
 * de EcoSuma mediante MaterialApi.
 */
class MaterialRepositoryImpl(
    private val materialApi: MaterialApi
) {

    /**
     * Consulta los materiales activos
     * registrados en PostgreSQL.
     */
    suspend fun obtenerMateriales(): List<MaterialDto> {
        return materialApi.obtenerMateriales()
    }

    /**
     * Consulta un material por su ID.
     */
    suspend fun obtenerMaterialPorId(id: Int): MaterialDto {
        return materialApi.obtenerMaterialPorId(id)
    }
}
