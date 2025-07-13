package com.rodolfo.hackaton.data.remote.api

import com.rodolfo.hackaton.data.remote.dto.request.LoginRequest
import com.rodolfo.hackaton.data.remote.dto.response.LoginResponse

interface ApiService {
    suspend fun login(request: LoginRequest): LoginResponse
}