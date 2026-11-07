
package com.example.myapplication1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.ecosuma.app.App
import com.ecosuma.app.UbicacionBridge

class MainActivity : ComponentActivity() {

    private var callbackUbicacion:
            ((Double, Double) -> Unit)? = null

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
                callbackUbicacion = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        UbicacionBridge.solicitarUbicacion = { callback ->
            callbackUbicacion = callback
            pedirPermisosUbicacion()
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

    private fun consultarUbicacion() {
        val ubicacion = obtenerUbicacion(this)

        if (ubicacion != null) {
            callbackUbicacion?.invoke(
                ubicacion.latitud,
                ubicacion.longitud
            )
        }

        callbackUbicacion = null
    }

    override fun onDestroy() {
        UbicacionBridge.solicitarUbicacion = null
        callbackUbicacion = null
        super.onDestroy()
    }
}
