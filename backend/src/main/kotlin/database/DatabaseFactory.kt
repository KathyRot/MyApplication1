package com.ecosuma.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object DatabaseFactory {

    private var dataSource: HikariDataSource? = null

    fun init() {
        if (dataSource != null) return

        val host = System.getenv("PGHOST")
            ?: throw IllegalStateException("Falta PGHOST")

        val port = System.getenv("PGPORT")
            ?: throw IllegalStateException("Falta PGPORT")

        val database = System.getenv("PGDATABASE")
            ?: throw IllegalStateException("Falta PGDATABASE")

        val username = System.getenv("PGUSER")
            ?: throw IllegalStateException("Falta PGUSER")

        val password = System.getenv("PGPASSWORD")
            ?: throw IllegalStateException("Falta PGPASSWORD")

        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://$host:$port/$database"

            this.username = username
            this.password = password

            driverClassName = "org.postgresql.Driver"

            maximumPoolSize = 5
            minimumIdle = 1
            connectionTimeout = 30000
            idleTimeout = 600000
            maxLifetime = 1800000
        }

        dataSource = HikariDataSource(config)

        println("EcoSuma: conexión con PostgreSQL inicializada")
    }

    fun getConnection(): Connection {
        return dataSource?.connection
            ?: throw IllegalStateException(
                "La conexión con PostgreSQL no está inicializada"
            )
    }

    fun testConnection(): Boolean {
        return try {
            getConnection().use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("SELECT 1").use { result ->
                        result.next() && result.getInt(1) == 1
                    }
                }
            }
        } catch (e: Exception) {
            System.err.println(
                "EcoSuma: error al comprobar PostgreSQL: ${e.message}"
            )
            false
        }
    }

    fun close() {
        dataSource?.close()
        dataSource = null
    }
}