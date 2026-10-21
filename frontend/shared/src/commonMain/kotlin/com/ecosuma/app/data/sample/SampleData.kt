package com.ecosuma.app.data.sample

/*
 * Datos de ejemplo tomados de la propuesta EcoSuma.
 * En esta etapa el examen solo pide login + navegación por rol, así que el catálogo
 * vive aquí. Más adelante cada lista vendrá de un endpoint del backend.
 */

data class Material(val nombre: String, val puntosPorKg: Int, val recomendacion: String)
data class Centro(val nombre: String, val direccion: String, val horario: String, val materiales: String)
data class Beneficio(val nombre: String, val costo: Int, val disponibles: Int)
data class Campana(val nombre: String, val fecha: String, val lugar: String, val metaKg: Int, val avanceKg: Int)
data class Indicador(val etiqueta: String, val valor: String)
data class EntregaReciente(val usuario: String, val material: String, val kg: Double, val puntos: Int, val fecha: String)

object SampleData {
    val materiales = listOf(
        Material("PET", 10, "Entregar limpio, vacío y aplastado."),
        Material("Cartón", 5, "Seco y doblado, sin restos de comida."),
        Material("Papel", 4, "Sin grapas ni papel encerado."),
        Material("Aluminio", 20, "Latas enjuagadas y aplastadas."),
        Material("Vidrio", 8, "Sin tapas, separado por color si es posible.")
    )

    val centros = listOf(
        Centro("Centro de Acopio 01", "Av. Juárez 120, Centro", "Lun a Sáb, 9:00 a 17:00", "PET, Cartón, Papel, Aluminio"),
        Centro("Punto Verde Parque Principal", "Parque principal, junto al kiosco", "Sáb y Dom, 9:00 a 14:00", "PET, Aluminio, Vidrio"),
        Centro("Centro de Acopio 02", "Calle Morelos 45, Jaltepec", "Lun a Vie, 8:00 a 15:00", "Todos los materiales")
    )

    val beneficios = listOf(
        Beneficio("Producto básico", 100, 20),
        Beneficio("Bolsa reutilizable", 60, 35),
        Beneficio("Kit de despensa básica", 300, 15),
        Beneficio("Descuento en comercio local", 150, 40)
    )

    val campanas = listOf(
        Campana("Campaña de recolección de PET", "30 de septiembre", "Parque principal", 500, 340),
        Campana("Reto Comunidad Verde", "Todo octubre", "Toda la ciudad", 1000, 410)
    )

    val indicadores = listOf(
        Indicador("Usuarios registrados", "1,250"),
        Indicador("Material recuperado", "2,840 kg"),
        Indicador("Entregas realizadas", "1,530"),
        Indicador("Puntos generados", "24,500"),
        Indicador("Beneficios canjeados", "320"),
        Indicador("Campañas activas", "4")
    )

    val entregasRecientes = listOf(
        EntregaReciente("Usuario 001", "PET", 3.0, 30, "22/09"),
        EntregaReciente("Usuario 002", "Cartón", 5.0, 25, "22/09"),
        EntregaReciente("Usuario 003", "Aluminio", 2.0, 40, "23/09")
    )
}