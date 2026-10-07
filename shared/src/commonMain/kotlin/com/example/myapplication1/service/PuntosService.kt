package com.example.myapplication1.service

import com.example.myapplication1.model.Material

class PuntosService {

    fun calcularPuntos(material: Material): Int {
        return (material.kilogramos * material.puntosPorKg).toInt()
    }
}