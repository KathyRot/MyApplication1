
package com.example.myapplication1

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ecosuma.app.UbicacionBridge

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MapaOpenStreetMap() {

    val context = LocalContext.current

    val webView = remember(context) {
        WebView(context).apply {

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            setBackgroundColor(Color.WHITE)

            // Mostrar mensajes del mapa en Logcat
            webChromeClient = object : WebChromeClient() {

                override fun onConsoleMessage(
                    consoleMessage: ConsoleMessage?
                ): Boolean {
                    Log.d(
                        "EcoSumaMapa",
                        "JavaScript: ${consoleMessage?.message()}"
                    )
                    return true
                }
            }

            // Detectar problemas de conexión
            webViewClient = object : WebViewClient() {

                override fun onLoadResource(
                    view: WebView?,
                    url: String?
                ) {
                    super.onLoadResource(view, url)

                    Log.d(
                        "EcoSumaMapa",
                        "Recurso solicitado: $url"
                    )
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {
                    super.onReceivedHttpError(
                        view,
                        request,
                        errorResponse
                    )

                    Log.e(
                        "EcoSumaMapa",
                        "HTTP ${errorResponse?.statusCode}: ${request?.url}"
                    )
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(
                        view,
                        request,
                        error
                    )

                    Log.e(
                        "EcoSumaMapa",
                        "Error WebView: ${error?.description}; " +
                                "URL: ${request?.url}"
                    )
                }
            }

            val html = """
                <!DOCTYPE html>
                <html lang="es">

                <head>
                    <meta charset="UTF-8">

                    <meta
                        name="viewport"
                        content="width=device-width, initial-scale=1.0"
                    >

                    <link
                        rel="stylesheet"
                        href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
                    >

                    <style>
                        html, body {
                            margin: 0;
                            padding: 0;
                            width: 100%;
                            height: 300px;
                            overflow: hidden;
                        }

                        #map {
                            display: block;
                            width: 100%;
                            height: 300px;
                            min-height: 300px;
                            background: #e7eee8;
                        }

                        #mensaje {
                            position: absolute;
                            top: 10px;
                            left: 10px;
                            right: 10px;
                            z-index: 9999;
                            background: white;
                            padding: 8px;
                            border-radius: 6px;
                            font-family: sans-serif;
                            font-size: 12px;
                        }

                        .boton-ubicacion {
                            width: 34px;
                            height: 34px;
                            background: white;
                            border: 2px solid rgba(0,0,0,0.2);
                            border-radius: 4px;
                            cursor: pointer;
                            font-size: 20px;
                        }
                    </style>
                </head>

                <body>

                    <div id="map"></div>

                    <div id="mensaje">
                        Cargando mapa de EcoSuma...
                    </div>

                    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

                    <script>

                        var mapa = null;
                        var marcadorUsuario = null;
                        var ubicacionPendiente = null;
                        var ultimaUbicacion = null;

                        // Centrar el mapa en la última ubicación GPS
                        function volverAMiUbicacion() {

                            if (mapa && ultimaUbicacion) {

                                mapa.setView(
                                    ultimaUbicacion,
                                    16
                                );

                                if (marcadorUsuario) {
                                    marcadorUsuario.openPopup();
                                }

                                console.log(
                                    'Mapa centrado en mi ubicación'
                                );

                            } else {

                                alert(
                                    'Todavía no se ha obtenido tu ubicación GPS.'
                                );
                            }
                        }

                        // Mostrar marcador del ciudadano
                        function mostrarUbicacion(latitud, longitud) {

                            ultimaUbicacion = [
                                latitud,
                                longitud
                            ];

                            if (!mapa) {
                                ubicacionPendiente = [
                                    latitud,
                                    longitud
                                ];
                                return;
                            }

                            mapa.setView(
                                [latitud, longitud],
                                16
                            );

                            if (marcadorUsuario) {

                                marcadorUsuario.setLatLng(
                                    [latitud, longitud]
                                );

                            } else {

                                marcadorUsuario = L.marker(
                                    [latitud, longitud]
                                ).addTo(mapa);

                                marcadorUsuario.bindPopup(
                                    'Estás aquí'
                                );
                            }

                            marcadorUsuario.openPopup();

                            console.log(
                                'Ubicación GPS mostrada: ' +
                                latitud + ', ' + longitud
                            );
                        }

                        try {

                            if (typeof L === 'undefined') {
                                throw new Error(
                                    'No se pudo cargar Leaflet'
                                );
                            }

                            // Mapa inicial de demostración: Pachuca
                            mapa = L.map('map').setView(
                                [20.1011, -98.7591],
                                13
                            );

                            // Imágenes de OpenStreetMap
                            var capa = L.tileLayer(
                                'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
                                {
                                    maxZoom: 19,
                                    attribution:
                                        '&copy; OpenStreetMap contributors'
                                }
                            );

                            capa.on('tileload', function(evento) {

                                console.log(
                                    'Imagen cargada: ' +
                                    evento.tile.src
                                );
                            });

                            capa.on('tileerror', function(evento) {

                                console.error(
                                    'Error al cargar imagen: ' +
                                    evento.tile.src
                                );
                            });

                            capa.addTo(mapa);

                            // Botón para regresar a mi ubicación
                            var botonUbicacion = L.control({
                                position: 'topleft'
                            });

                            botonUbicacion.onAdd = function() {

                                var boton = L.DomUtil.create(
                                    'button',
                                    'boton-ubicacion'
                                );

                                boton.innerHTML = '📍';
                                boton.title = 'Mi ubicación';
                                boton.setAttribute(
                                    'aria-label',
                                    'Mi ubicación'
                                );

                                L.DomEvent.disableClickPropagation(
                                    boton
                                );

                                L.DomEvent.disableScrollPropagation(
                                    boton
                                );

                                L.DomEvent.on(
                                    boton,
                                    'click',
                                    function(evento) {

                                        L.DomEvent.stop(evento);

                                        volverAMiUbicacion();
                                    }
                                );

                                return boton;
                            };

                            botonUbicacion.addTo(mapa);

                            // Ajustar el tamaño del mapa
                            mapa.whenReady(function() {

                                setTimeout(function() {

                                    mapa.invalidateSize(true);

                                    console.log(
                                        'Tamaño del mapa: ' +
                                        mapa.getSize().x + ' x ' +
                                        mapa.getSize().y
                                    );

                                    if (ubicacionPendiente) {

                                        mostrarUbicacion(
                                            ubicacionPendiente[0],
                                            ubicacionPendiente[1]
                                        );

                                        ubicacionPendiente = null;
                                    }

                                }, 500);
                            });

                            document.getElementById(
                                'mensaje'
                            ).style.display = 'none';

                            console.log(
                                'Leaflet inicializado correctamente'
                            );

                        } catch (error) {

                            console.error(
                                'Error al iniciar el mapa: ' +
                                error.message
                            );

                            document.getElementById(
                                'mensaje'
                            ).innerText =
                                'Error: ' + error.message;
                        }

                    </script>

                </body>
                </html>
            """.trimIndent()

            loadDataWithBaseURL(
                "https://unpkg.com/",
                html,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    // Obtener la ubicación del ciudadano
    LaunchedEffect(webView) {

        UbicacionBridge.obtenerUbicacion { latitud, longitud ->

            Log.d(
                "EcoSumaMapa",
                "GPS recibido: $latitud, $longitud"
            )

            webView.post {

                webView.evaluateJavascript(
                    "mostrarUbicacion($latitud, $longitud);",
                    null
                )
            }
        }
    }

    // Liberar recursos al cerrar el mapa
    DisposableEffect(webView) {

        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    // Mostrar mapa dentro de EcoSuma
    AndroidView(
        factory = { webView },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
