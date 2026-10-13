package com.example.myapplication1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform