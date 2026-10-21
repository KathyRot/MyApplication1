package com.example.myapplication1.ui.screens

import com.example.myapplication1.model.Material
import com.example.myapplication1.service.PuntosService
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {

    val material = Material(
        nombre = "PET",
        kilogramos = 3.0,
        puntosPorKg = 10
    )

    val servicio = PuntosService()

    val puntos = servicio.calcularPuntos(material)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8F3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Encabezado
        Text(
            text = "♻️ EcoSuma",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF287D3C)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Recicla, suma y recibe beneficios",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Tarjeta de puntos
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "MIS PUNTOS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = puntos.toString(),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF287D3C)
                )

                Text(
                    text = "puntos acumulados",
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "¿Qué quieres hacer?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("♻️ Reciclar")
            }

            Button(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text("🎁 Beneficios")
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📋 Ver mi historial")
        }
    }
}