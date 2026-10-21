package com.ecosuma.app.composeApp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Muestra las credenciales de prueba de EcoSuma.
 * Estas credenciales son únicamente para demostración.
 */
@Composable
fun DemoCredentialsCard() {

    val demo: kotlin.collections.List<Triple<String, String, String>> = listOf(
        Triple("maria", "maria123", "Ciudadano"),
        Triple("admin", "admin123", "Administrador"),
        Triple("acopio", "acopio123", "Acopio")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Usuarios de prueba",
                fontWeight = FontWeight.SemiBold
            )

            demo.forEach { (username, password, role) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$username / $password",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}