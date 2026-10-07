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

/** Usuarios de prueba: son los mismos que están en users.json del backend. */
@Composable
fun DemoCredentialsCard() {
    val demo = listOf(
        Triple("maria", "maria123", "Ciudadano"),
        Triple("admin", "admin123", "Administrador"),
        Triple("acopio", "acopio123", "Acopio")
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Usuarios de prueba", fontWeight = FontWeight.SemiBold)
            demo.forEach { (user, pass, rol) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("$user / $pass", style = MaterialTheme.typography.bodyMedium)
                    Text(rol, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}