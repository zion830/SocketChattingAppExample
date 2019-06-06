package com.example.network

object URL {
    private const val host = "35.221.98.14"
    private const val port = 8080

    fun get() = String.format("http://%s:%d", host, port)
}