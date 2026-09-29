package com.example.myapplication1

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyApplication1",
    ) {
        App()
    }
}