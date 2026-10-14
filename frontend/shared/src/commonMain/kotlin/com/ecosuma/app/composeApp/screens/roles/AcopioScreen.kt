package com.ecosuma.app.composeApp.screens.roles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ecosuma.app.composeApp.components.InfoCard
import com.ecosuma.app.composeApp.components.RoleScaffold
import com.ecosuma.app.composeApp.components.SectionTitle
import com.ecosuma.app.composeApp.theme.Points
import com.ecosuma.app.composeApp.theme.RoleColors
import com.ecosuma.app.data.sample.Material
import com.ecosuma.app.data.sample.SampleData
import com.ecosuma.app.domain.model.User

/**
 * Vista del Personal de centro de acopio: registrar entregas y calcular puntos.
 * En esta etapa el registro se muestra en pantalla, todavía no se envía al backend.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AcopioScreen(user: User, onLogout: () -> Unit) {
    var ciudadano by remember { mutableStateOf("") }
    var material by remember { mutableStateOf<Material?>(null) }
    var kgText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var ultimo by remember { mutableStateOf<String?>(null) }

    val kg = kgText.replace(",", ".").toDoubleOrNull()
    val elegido = material
    val puntos = if (elegido != null && kg != null) (elegido.puntosPorKg * kg).toInt() else 0

    RoleScaffold(user.centro ?: "Centro de acopio", user.fullName, RoleColors.Acopio, onLogout) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .widthIn(max = 520.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Registrar entrega", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = ciudadano,
                onValueChange = { ciudadano = it; error = null },
                label = { Text("Usuario del ciudadano") },
                supportingText = { Text("Más adelante se llenará al escanear su código QR") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SectionTitle("Material")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SampleData.materiales.forEach { m ->
                    FilterChip(
                        selected = material == m,
                        onClick = { material = m; error = null },
                        label = { Text("${m.nombre} (${m.puntosPorKg}/kg)") }
                    )
                }
            }

            OutlinedTextField(
                value = kgText,
                onValueChange = { kgText = it; error = null },
                label = { Text("Cantidad en kg") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Puntos a otorgar: $puntos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Points
            )

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    error = when {
                        ciudadano.isBlank() -> "Escribe el usuario del ciudadano"
                        elegido == null -> "Elige el material recibido"
                        kg == null || kg <= 0 -> "Escribe una cantidad mayor a 0"
                        else -> null
                    }
                    if (error == null && elegido != null) {
                        ultimo = "${ciudadano.trim()} · ${elegido.nombre} · $kg kg · $puntos puntos"
                        ciudadano = ""; material = null; kgText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Registrar entrega") }

            ultimo?.let {
                InfoCard("Entrega registrada", listOf(it), highlight = "Guardada en esta sesión")
            }
        }
    }
}