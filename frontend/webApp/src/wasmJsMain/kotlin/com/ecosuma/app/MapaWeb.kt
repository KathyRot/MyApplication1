package com.ecosuma.app

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLIFrameElement

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapaWeb() {

    WebElementView(
        factory = {

            (document.createElement("iframe") as HTMLIFrameElement).apply {

                src = "mapa.html"

                title = "Mapa de EcoSuma"

                setAttribute(
                    "allow",
                    "geolocation"
                )

                style.width = "100%"

                style.height = "100%"

                style.border = "none"

                style.borderRadius = "12px"
            }
        },

        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}