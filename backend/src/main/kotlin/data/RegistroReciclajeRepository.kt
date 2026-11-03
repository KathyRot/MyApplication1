package com.ecosuma.data

import com.ecosuma.database.DatabaseFactory
import com.ecosuma.models.RegistroReciclaje
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.time.OffsetDateTime

class RegistroReciclajeRepository {

    private fun convertirRegistro(rs: ResultSet): RegistroReciclaje {
        val estadoBD = rs.getString("estado")

        return RegistroReciclaje(
            id = rs.getInt("id_registro"),
            usuarioId = rs.getInt("id_usuario"),
            centroAcopioId = rs.getInt("id_centro"),
            materialId = rs.getInt("id_material"),
            cantidadKg = rs.getBigDecimal("cantidad").toDouble(),
            puntosObtenidos = rs.getBigDecimal("puntos_obtenidos").toInt(),
            fecha = rs.getObject(
                "fecha_registro",
                OffsetDateTime::class.java
            ).toString(),
            estado = if (estadoBD == "VALIDADO") "CONFIRMADO" else estadoBD,
            registradoPor = rs.getInt("id_usuario"),
            confirmadoPor = null
        )
    }

    private val columnas = """
        id_registro, id_usuario, id_material, id_centro,
        cantidad, puntos_obtenidos, fecha_registro, estado
    """.trimIndent()

    fun obtenerTodos(): List<RegistroReciclaje> {
        val sql = """
            SELECT $columnas
            FROM registros_reciclaje
            ORDER BY id_registro
        """.trimIndent()

        return consultarLista(sql)
    }

    fun buscarPorId(id: Int): RegistroReciclaje? {
        DatabaseFactory.getConnection().use { connection ->
            return buscarPorId(connection, id)
        }
    }

    private fun buscarPorId(
        connection: Connection,
        id: Int
    ): RegistroReciclaje? {
        val sql = """
            SELECT $columnas
            FROM registros_reciclaje
            WHERE id_registro = ?
        """.trimIndent()

        connection.prepareStatement(sql).use { statement ->
            statement.setInt(1, id)

            statement.executeQuery().use { rs ->
                return if (rs.next()) convertirRegistro(rs) else null
            }
        }
    }

    fun obtenerPorUsuario(usuarioId: Int): List<RegistroReciclaje> {
        val sql = """
            SELECT $columnas
            FROM registros_reciclaje
            WHERE id_usuario = ?
            ORDER BY fecha_registro DESC
        """.trimIndent()

        return consultarLista(sql, usuarioId)
    }

    fun obtenerPorCentro(centroAcopioId: Int): List<RegistroReciclaje> {
        val sql = """
            SELECT $columnas
            FROM registros_reciclaje
            WHERE id_centro = ?
            ORDER BY fecha_registro DESC
        """.trimIndent()

        return consultarLista(sql, centroAcopioId)
    }

    private fun consultarLista(
        sql: String,
        parametro: Int? = null
    ): List<RegistroReciclaje> {
        val registros = mutableListOf<RegistroReciclaje>()

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                if (parametro != null) {
                    statement.setInt(1, parametro)
                }

                statement.executeQuery().use { rs ->
                    while (rs.next()) {
                        registros.add(convertirRegistro(rs))
                    }
                }
            }
        }

        return registros
    }

    fun guardar(registro: RegistroReciclaje): RegistroReciclaje {
        val sql = """
            INSERT INTO registros_reciclaje (
                id_usuario, id_material, id_centro,
                cantidad, puntos_obtenidos, estado
            )
            VALUES (?, ?, ?, ?, 0, 'PENDIENTE')
            RETURNING id_registro
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setInt(1, registro.usuarioId)
                statement.setInt(2, registro.materialId)
                statement.setInt(3, registro.centroAcopioId)
                statement.setBigDecimal(
                    4,
                    java.math.BigDecimal.valueOf(registro.cantidadKg)
                )

                statement.executeQuery().use { rs ->
                    if (!rs.next()) {
                        throw SQLException("No se pudo guardar el registro")
                    }

                    val id = rs.getInt("id_registro")

                    return buscarPorId(connection, id)
                        ?: throw SQLException(
                            "No se encontró el registro guardado"
                        )
                }
            }
        }
    }

    fun actualizar(registro: RegistroReciclaje): Boolean {
        val estadoBD = when (registro.estado) {
            "CONFIRMADO" -> "VALIDADO"
            "PENDIENTE", "RECHAZADO" -> registro.estado
            else -> throw IllegalArgumentException("Estado inválido")
        }

        val sql = """
            UPDATE registros_reciclaje
            SET estado = ?, puntos_obtenidos = ?
            WHERE id_registro = ?
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, estadoBD)
                statement.setBigDecimal(
                    2,
                    java.math.BigDecimal.valueOf(
                        registro.puntosObtenidos.toLong()
                    )
                )
                statement.setInt(3, registro.id)

                return statement.executeUpdate() > 0
            }
        }
    }

    fun confirmar(
        id: Int,
        confirmadoPor: Int,
        puntos: Int
    ): RegistroReciclaje? {
        require(confirmadoPor > 0) {
            "El identificador del personal debe ser válido"
        }
        require(puntos >= 0) {
            "Los puntos no pueden ser negativos"
        }

        DatabaseFactory.getConnection().use { connection ->
            connection.autoCommit = false

            try {
                val registro = bloquearRegistro(connection, id)

                if (registro == null || registro.estado != "PENDIENTE") {
                    connection.rollback()
                    return null
                }

                val actualizarSql = """
                    UPDATE registros_reciclaje
                    SET estado = 'VALIDADO',
                        puntos_obtenidos = ?
                    WHERE id_registro = ?
                      AND estado = 'PENDIENTE'
                """.trimIndent()

                connection.prepareStatement(actualizarSql).use { statement ->
                    statement.setBigDecimal(
                        1,
                        java.math.BigDecimal.valueOf(puntos.toLong())
                    )
                    statement.setInt(2, id)

                    if (statement.executeUpdate() != 1) {
                        connection.rollback()
                        return null
                    }
                }

                if (puntos > 0) {
                    val movimientoSql = """
                        INSERT INTO movimientos_ecopuntos (
                            id_usuario, id_registro, tipo,
                            puntos, descripcion
                        )
                        VALUES (?, ?, 'GANADO', ?, ?)
                    """.trimIndent()

                    connection.prepareStatement(movimientoSql).use { statement ->
                        statement.setInt(1, registro.usuarioId)
                        statement.setInt(2, id)
                        statement.setBigDecimal(
                            3,
                            java.math.BigDecimal.valueOf(puntos.toLong())
                        )
                        statement.setString(
                            4,
                            "EcoPuntos por entrega de reciclaje"
                        )
                        statement.executeUpdate()
                    }
                }

                val confirmado = buscarPorId(connection, id)
                    ?: throw SQLException(
                        "No se encontró el registro confirmado"
                    )

                connection.commit()

                return confirmado
            } catch (e: Exception) {
                connection.rollback()
                throw e
            } finally {
                connection.autoCommit = true
            }
        }
    }

    fun rechazar(id: Int): RegistroReciclaje? {
        DatabaseFactory.getConnection().use { connection ->
            connection.autoCommit = false

            try {
                val registro = bloquearRegistro(connection, id)

                if (registro == null || registro.estado != "PENDIENTE") {
                    connection.rollback()
                    return null
                }

                val sql = """
                    UPDATE registros_reciclaje
                    SET estado = 'RECHAZADO',
                        puntos_obtenidos = 0
                    WHERE id_registro = ?
                      AND estado = 'PENDIENTE'
                """.trimIndent()

                connection.prepareStatement(sql).use { statement ->
                    statement.setInt(1, id)

                    if (statement.executeUpdate() != 1) {
                        connection.rollback()
                        return null
                    }
                }

                val rechazado = buscarPorId(connection, id)
                    ?: throw SQLException(
                        "No se encontró el registro rechazado"
                    )

                connection.commit()

                return rechazado
            } catch (e: Exception) {
                connection.rollback()
                throw e
            } finally {
                connection.autoCommit = true
            }
        }
    }

    private fun bloquearRegistro(
        connection: Connection,
        id: Int
    ): RegistroReciclaje? {
        val sql = """
            SELECT $columnas
            FROM registros_reciclaje
            WHERE id_registro = ?
            FOR UPDATE
        """.trimIndent()

        connection.prepareStatement(sql).use { statement ->
            statement.setInt(1, id)

            statement.executeQuery().use { rs ->
                return if (rs.next()) convertirRegistro(rs) else null
            }
        }
    }

    fun obtenerPuntosPorUsuario(usuarioId: Int): Long {
        val sql = """
            SELECT COALESCE(
                SUM(
                    CASE
                        WHEN tipo = 'GANADO' THEN puntos
                        WHEN tipo = 'CANJEADO' THEN -puntos
                        ELSE 0
                    END
                ), 0
            ) AS total
            FROM movimientos_ecopuntos
            WHERE id_usuario = ?
        """.trimIndent()

        DatabaseFactory.getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setInt(1, usuarioId)

                statement.executeQuery().use { rs ->
                    return if (rs.next()) {
                        rs.getBigDecimal("total").toLong()
                    } else {
                        0L
                    }
                }
            }
        }
    }
}