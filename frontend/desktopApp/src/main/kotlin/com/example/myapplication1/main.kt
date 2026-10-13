package com.example.myapplication1

import com.example.myapplication1.model.Material
import com.example.myapplication1.service.PuntosService

fun main() {

    val material = Material(
        nombre = "PET",
        kilogramos = 3.0,
        puntosPorKg = 10
    )

    val servicio = PuntosService()

    val puntos = servicio.calcularPuntos(material)

    println("Material: ${material.nombre}")
    println("Kilogramos: ${material.kilogramos}")
    println("Puntos obtenidos: $puntos")
}