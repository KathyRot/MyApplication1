package com.ecosuma.data

import com.ecosuma.database.DatabaseFactory
import com.ecosuma.models.Material

class MaterialRepository {

    // Consultar materiales activos desde PostgreSQL
    fun obtenerTodos(): List<Material> {

        val materiales = mutableListOf<Material>()

        val sql = """
            SELECT
                id_material,
                nombre,
                puntos_por_unidad,
                activo
            FROM materiales
            WHERE activo = true
            ORDER BY id_material
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.executeQuery().use { resultado ->

                    while (resultado.next()) {

                        materiales.add(
                            Material(
                                id = resultado.getInt("id_material"),
                                nombre = resultado.getString("nombre"),
                                categoria = "Sin categoría",
                                puntosPorKg = resultado
                                    .getBigDecimal("puntos_por_unidad")
                                    .toInt(),
                                activo = resultado.getBoolean("activo")
                            )
                        )
                    }
                }
            }
        }

        return materiales
    }

    // Buscar un material activo por ID
    fun buscarPorId(id: Int): Material? {

        val sql = """
            SELECT
                id_material,
                nombre,
                puntos_por_unidad,
                activo
            FROM materiales
            WHERE id_material = ?
              AND activo = true
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.setInt(1, id)

                statement.executeQuery().use { resultado ->

                    if (resultado.next()) {

                        return Material(
                            id = resultado.getInt("id_material"),
                            nombre = resultado.getString("nombre"),
                            categoria = "Sin categoría",
                            puntosPorKg = resultado
                                .getBigDecimal("puntos_por_unidad")
                                .toInt(),
                            activo = resultado.getBoolean("activo")
                        )
                    }
                }
            }
        }

        return null
    }
}