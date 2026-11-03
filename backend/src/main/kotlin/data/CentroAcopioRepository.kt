package com.ecosuma.data

import com.ecosuma.database.DatabaseFactory
import com.ecosuma.models.CentroAcopio
import java.sql.ResultSet

class CentroAcopioRepository {

    // Convertir los datos de PostgreSQL al modelo Kotlin
    private fun convertirCentro(resultado: ResultSet): CentroAcopio {
        return CentroAcopio(
            id = resultado.getInt("id_centro"),
            nombre = resultado.getString("nombre"),
            direccion = resultado.getString("direccion"),
            latitud = resultado.getObject("latitud")?.let {
                (it as Number).toDouble()
            },
            longitud = resultado.getObject("longitud")?.let {
                (it as Number).toDouble()
            },
            horario = resultado.getString("horario") ?: "",
            telefono = resultado.getString("telefono") ?: "",
            activo = resultado.getBoolean("activo")
        )
    }

    // Obtener todos los centros activos con coordenadas
    fun obtenerTodos(): List<CentroAcopio> {

        val centros = mutableListOf<CentroAcopio>()

        val sql = """
            SELECT id_centro, nombre, direccion,
                   latitud, longitud, horario, telefono, activo
            FROM centros_acopio
            WHERE activo = true
              
            ORDER BY id_centro
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.executeQuery().use { resultado ->
                    while (resultado.next()) {
                        centros.add(convertirCentro(resultado))
                    }
                }
            }
        }

        return centros
    }

    // Buscar un centro activo por ID
    fun buscarPorId(id: Int): CentroAcopio? {

        val sql = """
            SELECT id_centro, nombre, direccion,
                   latitud, longitud, horario, telefono, activo
            FROM centros_acopio
            WHERE id_centro = ?
              AND activo = true
              
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setInt(1, id)

                statement.executeQuery().use { resultado ->
                    if (resultado.next()) {
                        return convertirCentro(resultado)
                    }
                }
            }
        }

        return null
    }
}