package com.ecosuma.app.composeApp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Título de sección dentro de una vista. */
@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

/** Número grande con su etiqueta (puntos, kg reciclados, indicadores). */
@Composable
fun StatCard(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = valueColor)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** Tarjeta genérica: título, una línea destacada opcional y líneas de detalle. */
@Composable
fun InfoCard(
    title: String,
    lines: List<String>,
    highlight: String? = null,
    highlightColor: Color = MaterialTheme.colorScheme.primary
) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            highlight?.let { Text(it, color = highlightColor, fontWeight = FontWeight.Bold) }
            lines.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

/** Campaña o reto con barra de avance hacia la meta en kg. */
@Composable
fun GoalCard(title: String, subtitle: String, current: Int, goal: Int) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(
                progress = { (current.toFloat() / goal).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
            Text("$current de $goal kg", style = MaterialTheme.typography.bodySmall)
        }
    }
}