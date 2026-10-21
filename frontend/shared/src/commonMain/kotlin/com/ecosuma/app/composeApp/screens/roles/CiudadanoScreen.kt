package com.ecosuma.app.composeApp.screens.roles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosuma.app.composeApp.components.GoalCard
import com.ecosuma.app.composeApp.components.InfoCard
import com.ecosuma.app.composeApp.components.RoleScaffold
import com.ecosuma.app.composeApp.components.SectionTitle
import com.ecosuma.app.composeApp.components.StatCard
import com.ecosuma.app.composeApp.theme.Points
import com.ecosuma.app.composeApp.theme.RoleColors
import com.ecosuma.app.data.sample.SampleData
import com.ecosuma.app.domain.model.User

private val tabs = listOf("Inicio", "Materiales", "Centros", "Beneficios")

/** Vista del rol Ciudadano: perfil con puntos, materiales, centros y beneficios. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadanoScreen(user: User, onLogout: () -> Unit) {
    var selected by rememberSaveable { mutableIntStateOf(0) }

    RoleScaffold("EcoSuma", user.fullName, RoleColors.Ciudadano, onLogout) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            PrimaryTabRow(selectedTabIndex = selected) {
                tabs.forEachIndexed { i, title ->
                    Tab(selected = selected == i, onClick = { selected = i }, text = { Text(title) })
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (selected) {
                    0 -> inicio(user)
                    1 -> items(SampleData.materiales) {
                        InfoCard(it.nombre, listOf(it.recomendacion), highlight = "${it.puntosPorKg} puntos por kg")
                    }
                    2 -> items(SampleData.centros) {
                        InfoCard(it.nombre, listOf(it.direccion, it.horario, "Recibe: ${it.materiales}"))
                    }
                    else -> {
                        item {
                            Text(
                                "Tienes ${user.puntosDisponibles} puntos disponibles",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(SampleData.beneficios) {
                            val alcanza = user.puntosDisponibles >= it.costo
                            InfoCard(
                                title = it.nombre,
                                lines = listOf("Disponibles: ${it.disponibles}"),
                                highlight = "${it.costo} puntos" +
                                        if (alcanza) "" else "  (te faltan ${it.costo - user.puntosDisponibles})",
                                highlightColor = if (alcanza) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Pestaña "Inicio": saludo, indicadores personales y campañas activas. */
private fun LazyListScope.inicio(user: User) {
    item {
        Column {
            Text(
                "Hola, ${user.fullName.substringBefore(" ")}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            user.colonia?.let { Text("Colonia $it", style = MaterialTheme.typography.bodyMedium) }
        }
    }
    item {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard("Puntos disponibles", user.puntosDisponibles.toString(), valueColor = Points, modifier = Modifier.weight(1f))
            StatCard("Material reciclado", "${user.kgReciclados} kg", modifier = Modifier.weight(1f))
        }
    }
    item {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard("Entregas realizadas", user.entregas.toString(), modifier = Modifier.weight(1f))
            StatCard("Beneficios utilizados", user.beneficiosUsados.toString(), modifier = Modifier.weight(1f))
        }
    }
    item { SectionTitle("Campañas activas") }
    items(SampleData.campanas) {
        GoalCard(it.nombre, "${it.fecha} · ${it.lugar}", it.avanceKg, it.metaKg)
    }
}