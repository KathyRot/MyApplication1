
package com.example.myapplication1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import androidx.core.content.ContextCompat

data class Ubicacion(
    val latitud: Double,
    val longitud: Double
)

fun obtenerUbicacionActual(
    context: Context,
    onResultado: (Ubicacion?) -> Unit
) {
    val permisoPreciso = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val permisoAproximado = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!permisoPreciso && !permisoAproximado) {
        onResultado(null)
        return
    }

    val locationManager = context.getSystemService(
        Context.LOCATION_SERVICE
    ) as LocationManager

    val proveedores = if (permisoPreciso) {
        listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER
        )
    } else {
        listOf(LocationManager.NETWORK_PROVIDER)
    }

    val proveedor = proveedores.firstOrNull {
        locationManager.isProviderEnabled(it)
    }

    if (proveedor == null) {
        onResultado(null)
        return
    }

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManager.getCurrentLocation(
                proveedor,
                CancellationSignal(),
                context.mainExecutor
            ) { location ->
                onResultado(
                    location?.let {
                        Ubicacion(
                            latitud = it.latitude,
                            longitud = it.longitude
                        )
                    }
                )
            }
        } else {
            val location = locationManager.getLastKnownLocation(
                proveedor
            )

            onResultado(
                location?.let {
                    Ubicacion(
                        latitud = it.latitude,
                        longitud = it.longitude
                    )
                }
            )
        }
    } catch (e: SecurityException) {
        onResultado(null)
    } catch (e: IllegalArgumentException) {
        onResultado(null)
    }
}
