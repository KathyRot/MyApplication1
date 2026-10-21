package com.example.myapplication1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {

    var nombre by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var asignatura by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var mostrarError by remember { mutableStateOf(false) }
    var mostrarCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Formulario de entrega"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // NOMBRE
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mostrarError = false
            },
            label = {
                Text("Nombre")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // MATRÍCULA
        OutlinedTextField(
            value = matricula,
            onValueChange = {
                matricula = it
                mostrarError = false
            },
            label = {
                Text("Matrícula")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ASIGNATURA
        OutlinedTextField(
            value = asignatura,
            onValueChange = {
                asignatura = it
                mostrarError = false
            },
            label = {
                Text("Asignatura")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // HORA
        OutlinedTextField(
            value = hora,
            onValueChange = {
                hora = it
                mostrarError = false
            },
            label = {
                Text("Hora que se imparte")
            },
            placeholder = {
                Text("Ejemplo: 10:00")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // FECHA
        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                mostrarError = false
            },
            label = {
                Text("Fecha de entrega")
            },
            placeholder = {
                Text("Ejemplo: 25/09/2026")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // MENSAJE DE ERROR
        if (mostrarError) {

            Text(
                text = "Por favor, completa todos los campos."
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // BOTÓN GUARDAR
        Button(
            onClick = {

                if (
                    nombre.isBlank() ||
                    matricula.isBlank() ||
                    asignatura.isBlank() ||
                    hora.isBlank() ||
                    fecha.isBlank()
                ) {

                    mostrarError = true
                    mostrarCard = false

                } else {

                    mostrarError = false
                    mostrarCard = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // CARD
        if (mostrarCard) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text("Datos registrados")

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text("Nombre: $nombre")

                    Text("Matrícula: $matricula")

                    Text("Asignatura: $asignatura")

                    Text("Hora que se imparte: $hora")

                    Text("Fecha de entrega: $fecha")
                }
            }
        }
    }
}