
package com.ecosuma.app.data.remote

import com.ecosuma.app.BASE_URL
import com.ecosuma.app.data.model.MaterialDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Comunicación con el backend de EcoSuma
 * para consultar materiales reciclables.
 */
class MaterialApi(
    private val client: HttpClient
) {

    /**
     * GET /api/materiales
     * Consulta los materiales disponibles.
     */
    suspend fun obtenerMateriales(): List<MaterialDto> {
        return client.get("$BASE_URL/api/materiales").body()
    }

    /**
     * GET /api/materiales/{id}
     * Consulta un material específico.
     */
    suspend fun obtenerMaterialPorId(id: Int): MaterialDto {
        return client.get("$BASE_URL/api/materiales/$id").body()
    }
}

