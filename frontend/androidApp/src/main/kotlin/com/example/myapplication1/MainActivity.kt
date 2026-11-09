
package com.example.myapplication1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.ecosuma.app.App
import com.ecosuma.app.MapaBridge
import com.ecosuma.app.UbicacionBridge

class MainActivity : ComponentActivity() {

    private var callbackUbicacion:
            ((Double, Double) -> Unit)? = null

    // Solicitar permisos GPS
    private val solicitarPermisosUbicacion =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->

            val preciso =
                permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val aproximado =
                permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (preciso || aproximado) {
                consultarUbicacion()
            } else {
                Log.d(
                    "EcoSumaMapa",
                    "El usuario no concedió permisos GPS"
                )
                callbackUbicacion = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Conectar el GPS con EcoSuma
        UbicacionBridge.solicitarUbicacion = { callback ->
            callbackUbicacion = callback
            pedirPermisosUbicacion()
        }

        // Conectar OpenStreetMap con EcoSuma
        MapaBridge.mostrarMapa = {
            MapaOpenStreetMap()
        }

        setContent {
            App()
        }
    }

    private fun pedirPermisosUbicacion() {

        val preciso = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val aproximado = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (preciso || aproximado) {
            consultarUbicacion()
        } else {
            solicitarPermisosUbicacion.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Obtener ubicación GPS actual
    private fun consultarUbicacion() {

        obtenerUbicacionActual(this) { ubicacion ->

            if (ubicacion != null) {

                Log.d(
                    "EcoSumaMapa",
                    "GPS obtenido: " +
                            "${ubicacion.latitud}, ${ubicacion.longitud}"
                )

                callbackUbicacion?.invoke(
                    ubicacion.latitud,
                    ubicacion.longitud
                )

            } else {

                Log.d(
                    "EcoSumaMapa",
                    "No se pudo obtener la ubicación GPS actual"
                )
            }

            callbackUbicacion = null
        }
    }

    override fun onDestroy() {

        UbicacionBridge.solicitarUbicacion = null
        MapaBridge.mostrarMapa = null
        callbackUbicacion = null

        super.onDestroy()
    }
}
