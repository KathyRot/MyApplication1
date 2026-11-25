package com.ecosuma.app

@OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
@JsFun("""
    (onSuccess, onError) => {
        if (!navigator.geolocation) {
            onError("La geolocalización no está disponible en este navegador.");
            return;
        }

        navigator.geolocation.getCurrentPosition(
            (position) => {
                onSuccess(
                    position.coords.latitude,
                    position.coords.longitude
                );
            },
            (error) => {
                switch (error.code) {
                    case 1:
                        onError("Permiso de ubicación denegado. Actívalo en la configuración del navegador.");
                        break;

                    case 2:
                        onError("No se pudo obtener tu ubicación. Verifica que esté activada.");
                        break;

                    case 3:
                        onError("Se agotó el tiempo para obtener tu ubicación. Inténtalo nuevamente.");
                        break;

                    default:
                        onError("Ocurrió un error al obtener tu ubicación.");
                }
            },
            {
                enableHighAccuracy: true,
                timeout: 15000,
                maximumAge: 0
            }
        );
    }
""")
private external fun obtenerUbicacionNavegador(
    onSuccess: (Double, Double) -> Unit,
    onError: (String) -> Unit
)

object UbicacionWeb {

    fun solicitar(
        onResultado: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        obtenerUbicacionNavegador(
            onResultado,
            onError
        )
    }
}