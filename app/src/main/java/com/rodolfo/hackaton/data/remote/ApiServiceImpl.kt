package com.rodolfo.hackaton.data.remote

import com.rodolfo.hackaton.data.remote.api.ApiConfig
import com.rodolfo.hackaton.data.remote.api.ApiService
import com.rodolfo.hackaton.data.remote.dto.request.LoginRequest
import com.rodolfo.hackaton.data.remote.dto.response.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okio.IOException

class ApiServiceImpl(
    private val apiClient: HttpClient
): ApiService {
    private val baseUrl = ApiConfig.BASE_URL


    override suspend fun login(request: LoginRequest): LoginResponse {
        return safeApiCall {
            val response = apiClient.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.value in 200..299) {
                response.body()
            } else {
                val errorBody = response.bodyAsText()
                throw Exception(parseErrorMessage(errorBody))
            }
        }
    }

    private fun parseErrorMessage(json: String): String {
        return try {
            val jsonObject = Json.parseToJsonElement(json).jsonObject
            jsonObject["message"]?.jsonPrimitive?.content
                ?: "Error desconocido"
        } catch (e: Exception) {
            "Error inesperado del servidor"
        }
    }

    private suspend inline fun <reified T> safeApiCall(crossinline apiCall: suspend () -> T): T {
        try {
            return apiCall()
        } catch (e: IOException) {
            throw Exception("Tiempo de espera agotado. Por favor verifique su conexión a Internet o contacte al soporte técnico")
        } catch (e: SocketTimeoutException) {
            throw Exception("Tiempo de espera agotado. Por favor verifique su conexión a Internet o contacte al soporte técnico")
        } catch (e: ConnectTimeoutException) {
            throw Exception("No se pudo establecer conexión. Revise su red o contacte con el soporte técnico.")
        } catch (e: TimeoutCancellationException) {
            throw Exception("Tiempo de espera agotado. Por favor verifique su conexión a Internet o contacte al soporte técnico")
        }
    }
}