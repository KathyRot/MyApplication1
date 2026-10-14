package com.ecosuma.app.composeApp.screens.roles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosuma.app.composeApp.components.InfoCard
import com.ecosuma.app.composeApp.components.RoleScaffold
import com.ecosuma.app.composeApp.components.SectionTitle
import com.ecosuma.app.composeApp.components.StatCard
import com.ecosuma.app.composeApp.theme.RoleColors
import com.ecosuma.app.data.sample.SampleData
import com.ecosuma.app.domain.model.User

private val modulos = listOf(
    "Usuarios" to "Consultar, buscar y suspender cuentas",
    "Materiales" to "Agregar materiales y definir puntos por kg",
    "Centros de acopio" to "Horarios, ubicación y materiales aceptados",
    "Beneficios" to "Costo en puntos, existencias y vigencia",
    "Campañas" to "Publicar campañas y retos",
    "Entregas" to "Historial de todas las entregas"
)

/** Vista del rol Administrador: dashboard con indicadores, entregas recientes y módulos. */
@Composable
fun AdministradorScreen(user: User, onLogout: () -> Unit) {
    RoleScaffold("Administración EcoSuma", user.fullName, RoleColors.Administrador, onLogout) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            fullWidth {
                Text("Panel general", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            items(SampleData.indicadores) { StatCard(it.etiqueta, it.valor, valueColor = RoleColors.Administrador) }

            fullWidth { SectionTitle("Entregas recientes") }
            items(SampleData.entregasRecientes) {
                InfoCard(it.usuario, listOf("${it.kg} kg · ${it.fecha}"), highlight = "${it.material} · ${it.puntos} pts")
            }

            fullWidth { SectionTitle("Módulos") }
            items(modulos) { (titulo, desc) -> InfoCard(titulo, listOf(desc)) }
        }
    }
}

/** Un elemento que ocupa todo el ancho de la cuadrícula (para títulos). */
private fun LazyGridScope.fullWidth(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) { content() }
}