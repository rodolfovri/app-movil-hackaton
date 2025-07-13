package com.rodolfo.hackaton.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

object ApiClient {
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    connectTimeout(30, TimeUnit.SECONDS)
                    readTimeout(30, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    retryOnConnectionFailure(true)
                }
            }

            // Plugin de timeout de Ktor
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        val sanitizedMessage = message.replace(
                            Regex("https?://[^\\s/]+"),
                            "[URL_PROTEGIDA]"
                        )
                        println("KTOR_CLIENT: $sanitizedMessage")
                    }
                }
            }

            install(ResponseObserver) {
                onResponse { response ->
                    if (response.status != HttpStatusCode.OK ||
                        response.headers["Content-Type"]?.contains("text/html") == true) {
                        println("KTOR_CLIENT: Tipo de respuesta inesperado - Estado: ${response.status}")
                    }
                }
            }

            expectSuccess = false
        }
    }
}