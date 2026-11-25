package com.example.myapplication1

class WasmPlatform : Platform {
    override val name: String = "WebAssembly - Navegador"
}

actual fun getPlatform(): Platform = WasmPlatform()