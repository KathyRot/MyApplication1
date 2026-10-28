package com.ecosuma.data

import com.ecosuma.models.Material

class MaterialRepository {

    // Materiales de prueba para EcoSuma
    private val materiales = listOf(
        Material(
            id = 1,
            nombre = "PET",
            categoria = "Plástico",
            puntosPorKg = 10
        ),
        Material(
            id = 2,
            nombre = "Cartón",
            categoria = "Papel y cartón",
            puntosPorKg = 5
        ),
        Material(
            id = 3,
            nombre = "Aluminio",
            categoria = "Metal",
            puntosPorKg = 20
        ),
        Material(
            id = 4,
            nombre = "Vidrio",
            categoria = "Vidrio",
            puntosPorKg = 8
        ),
        Material(
            id = 5,
            nombre = "Papel",
            categoria = "Papel y cartón",
            puntosPorKg = 5
        )
    )

    // Devuelve los materiales disponibles
    fun obtenerTodos(): List<Material> {
        return materiales.filter { it.activo }
    }

    // Busca un material mediante su ID
    fun buscarPorId(id: Int): Material? {
        return materiales.find { it.id == id && it.activo }
    }
}