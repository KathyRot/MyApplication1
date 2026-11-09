
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecosuma.app.MapaBridge
import com.ecosuma.app.UbicacionBridge
import com.ecosuma.app.composeApp.components.GoalCard
import com.ecosuma.app.composeApp.components.InfoCard
import com.ecosuma.app.composeApp.components.RoleScaffold
import com.ecosuma.app.composeApp.components.SectionTitle
import com.ecosuma.app.composeApp.components.StatCard
import com.ecosuma.app.composeApp.theme.Points
import com.ecosuma.app.composeApp.theme.RoleColors
import com.ecosuma.app.data.model.CentroAcopioDto
import com.ecosuma.app.data.model.CentroCercanoDto
import com.ecosuma.app.data.model.MaterialDto
import com.ecosuma.app.data.sample.SampleData
import com.ecosuma.app.di.AppContainer
import com.ecosuma.app.domain.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

private val tabs = listOf(
    "Inicio",
    "Materiales",
    "Centros",
    "Beneficios"
)

// Datos ficticios para mostrar la interfaz sin PostgreSQL.
private val materialesDemo = listOf(
    MaterialDto(1, "PET", "Plástico", 10),
    MaterialDto(2, "Cartón", "Papel y cartón", 5),
    MaterialDto(3, "Aluminio", "Metal", 20),
    MaterialDto(4, "Vidrio", "Vidrio", 8),
    MaterialDto(5, "Papel", "Papel y cartón", 6)
)

private val centrosDemo = listOf(
    CentroAcopioDto(
        id = 1,
        nombre = "Centro de demostración Norte",
        direccion = "Dirección ficticia (demo)",
        horario = "Horario de ejemplo",
        telefono = "No disponible"
    ),
    CentroAcopioDto(
        id = 2,
        nombre = "Centro de demostración Sur",
        direccion = "Dirección ficticia (demo)",
        horario = "Horario de ejemplo",
        telefono = "No disponible"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadanoScreen(
    user: User,
    onLogout: () -> Unit
) {
    var selected by rememberSaveable {
        mutableIntStateOf(0)
    }

    // MATERIALES

    var materiales by remember {
        mutableStateOf<List<MaterialDto>>(emptyList())
    }

    var cargandoMateriales by remember {
        mutableStateOf(false)
    }

    var errorMateriales by remember {
        mutableStateOf<String?>(null)
    }

    // CENTROS

    var centros by remember {
        mutableStateOf<List<CentroAcopioDto>>(emptyList())
    }

    var cargandoCentros by remember {
        mutableStateOf(false)
    }

    var errorCentros by remember {
        mutableStateOf<String?>(null)
    }

    // GEOLOCALIZACIÓN

    var centrosCercanos by remember {
        mutableStateOf<List<CentroCercanoDto>>(emptyList())
    }

    var buscandoCercanos by remember {
        mutableStateOf(false)
    }

    var errorUbicacion by remember {
        mutableStateOf<String?>(null)
    }

    var mostrarCercanos by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    // CONSULTAR MATERIALES

    LaunchedEffect(selected) {
        if (selected == 1) {
            cargandoMateriales = true
            errorMateriales = null

            try {
                materiales = AppContainer.materialRepository
                    .obtenerMateriales()

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                materiales = materialesDemo
                errorMateriales =
                    "Mostrando materiales de demostración (sin conexión al servidor)."

            } finally {
                cargandoMateriales = false
            }
        }
    }

    // CONSULTAR CENTROS

    LaunchedEffect(selected) {
        if (selected == 2) {
            cargandoCentros = true
            errorCentros = null

            try {
                centros = AppContainer.centroAcopioRepository
                    .obtenerCentros()

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                centros = centrosDemo
                errorCentros =
                    "Mostrando centros ficticios de demostración (sin conexión al servidor)."

            } finally {
                cargandoCentros = false
            }
        }
    }

    // BUSCAR CENTROS CERCANOS POR GPS

    fun buscarCentrosCercanos() {
        if (buscandoCercanos) return

        errorUbicacion = null
        buscandoCercanos = true

        if (UbicacionBridge.solicitarUbicacion == null) {
            errorUbicacion =
                "La ubicación no está disponible en este dispositivo."
            buscandoCercanos = false
            return
        }

        UbicacionBridge.obtenerUbicacion { latitud, longitud ->
            coroutineScope.launch {
                try {
                    centrosCercanos =
                        AppContainer.centroAcopioRepository
                            .obtenerCentrosCercanos(
                                latitud = latitud,
                                longitud = longitud
                            )

                    mostrarCercanos = true

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {
                    errorUbicacion =
                        "No se pudieron consultar los centros cercanos. " +
                                "Verifica la conexión con el servidor."

                } finally {
                    buscandoCercanos = false
                }
            }
        }
    }

    RoleScaffold(
        "EcoSuma",
        user.fullName,
        RoleColors.Ciudadano,
        onLogout
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            PrimaryTabRow(
                selectedTabIndex = selected
            ) {
                tabs.forEachIndexed { i, title ->
                    Tab(
                        selected = selected == i,
                        onClick = { selected = i },
                        text = { Text(title) }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                when (selected) {

                    // INICIO
                    0 -> inicio(user)

                    // MATERIALES
                    1 -> {

                        if (errorMateriales != null) {
                            item {
                                Text(
                                    text = errorMateriales ?: "",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        when {
                            cargandoMateriales -> {
                                item {
                                    Text("Cargando materiales...")
                                }
                            }

                            materiales.isEmpty() -> {
                                item {
                                    Text("No hay materiales disponibles.")
                                }
                            }

                            else -> {
                                items(materiales) { material ->
                                    InfoCard(
                                        title = material.nombre,
                                        lines = listOf(
                                            "Categoría: ${material.categoria}"
                                        ),
                                        highlight =
                                            "${material.puntosPorKg} puntos por kg"
                                    )
                                }
                            }
                        }
                    }

                    // CENTROS DE ACOPIO
                    2 -> {

                        item {
                            Column(
                                verticalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {
                                SectionTitle("Centros de acopio")

                                Text(
                                    text = "Ubicación de los centros de acopio",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                // Mapa interactivo de OpenStreetMap
                                MapaBridge.Mapa()

                                Text(
                                    text = "Mapa de demostración centrado en Pachuca. " +
                                            "Los centros reales aparecerán cuando " +
                                            "se disponga de sus coordenadas.",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Button(
                                    onClick = {
                                        buscarCentrosCercanos()
                                    },
                                    enabled = !buscandoCercanos,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Buscar centros cercanos")
                                }

                                if (mostrarCercanos) {
                                    OutlinedButton(
                                        onClick = {
                                            mostrarCercanos = false
                                            errorUbicacion = null
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Ver todos los centros")
                                    }
                                }

                                if (buscandoCercanos) {
                                    Text("Buscando centros cercanos...")
                                }

                                if (errorUbicacion != null) {
                                    Text(
                                        text = errorUbicacion ?: "",
                                        color =
                                            MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        if (mostrarCercanos) {

                            if (centrosCercanos.isEmpty()) {
                                item {
                                    Text(
                                        "No se encontraron centros cercanos " +
                                                "con ubicación registrada."
                                    )
                                }

                            } else {
                                items(centrosCercanos) { centro ->
                                    InfoCard(
                                        title = centro.nombre,
                                        lines = listOf(
                                            "Dirección: ${centro.direccion}",
                                            "Horario: ${centro.horario}",
                                            "Teléfono: ${centro.telefono}"
                                        ),
                                        highlight =
                                            "Distancia aproximada: " +
                                                    "${centro.distanciaKm} km"
                                    )
                                }
                            }

                        } else {

                            if (errorCentros != null) {
                                item {
                                    Text(
                                        text = errorCentros ?: "",
                                        color =
                                            MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            when {
                                cargandoCentros -> {
                                    item {
                                        Text(
                                            "Cargando centros de acopio..."
                                        )
                                    }
                                }

                                centros.isEmpty() -> {
                                    item {
                                        Text(
                                            "No hay centros de acopio disponibles."
                                        )
                                    }
                                }

                                else -> {
                                    items(centros) { centro ->
                                        InfoCard(
                                            title = centro.nombre,
                                            lines = listOf(
                                                "Dirección: ${centro.direccion}",
                                                "Horario: ${centro.horario}",
                                                "Teléfono: ${centro.telefono}"
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // BENEFICIOS
                    else -> {

                        item {
                            Text(
                                text =
                                    "Tienes ${user.puntosDisponibles} puntos disponibles",
                                style =
                                    MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(SampleData.beneficios) { beneficio ->

                            val alcanza =
                                user.puntosDisponibles >= beneficio.costo

                            InfoCard(
                                title = beneficio.nombre,
                                lines = listOf(
                                    "Disponibles: ${beneficio.disponibles}"
                                ),
                                highlight =
                                    "${beneficio.costo} puntos" +
                                            if (alcanza) {
                                                ""
                                            } else {
                                                "  (te faltan ${beneficio.costo - user.puntosDisponibles})"
                                            },
                                highlightColor =
                                    if (alcanza) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

// PANTALLA DE INICIO

private fun LazyListScope.inicio(user: User) {

    item {
        Column {
            Text(
                text =
                    "Hola, ${user.fullName.substringBefore(" ")}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            user.colonia?.let {
                Text(
                    text = "Colonia $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    item {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                "Puntos disponibles",
                user.puntosDisponibles.toString(),
                valueColor = Points,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                "Material reciclado",
                "${user.kgReciclados} kg",
                modifier = Modifier.weight(1f)
            )
        }
    }

    item {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(
                "Entregas realizadas",
                user.entregas.toString(),
                modifier = Modifier.weight(1f)
            )

            StatCard(
                "Beneficios utilizados",
                user.beneficiosUsados.toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }

    item {
        SectionTitle("Campañas activas")
    }

    items(SampleData.campanas) {
        GoalCard(
            it.nombre,
            "${it.fecha} · ${it.lugar}",
            it.avanceKg,
            it.metaKg
        )
    }
}
