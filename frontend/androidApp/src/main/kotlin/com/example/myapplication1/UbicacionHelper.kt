
package com.example.myapplication1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat

data class Ubicacion(
    val latitud: Double,
    val longitud: Double
)

fun obtenerUbicacion(context: Context): Ubicacion? {

    val permisoPreciso = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val permisoAproximado = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!permisoPreciso && !permisoAproximado) {
        return null
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

    for (proveedor in proveedores) {
        try {
            if (locationManager.isProviderEnabled(proveedor)) {

                val ubicacion =
                    locationManager.getLastKnownLocation(proveedor)

                if (ubicacion != null) {
                    return Ubicacion(
                        latitud = ubicacion.latitude,
                        longitud = ubicacion.longitude
                    )
                }
            }
        } catch (e: SecurityException) {
            return null
        } catch (e: IllegalArgumentException) {
            continue
        }
    }

    return null
}
