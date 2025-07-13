package com.rodolfo.hackaton.domain.repository

import com.rodolfo.hackaton.data.remote.dto.request.LoginRequest
import com.rodolfo.hackaton.domain.model.LoginResult

interface AuthRepository {
    suspend fun login(request: LoginRequest) : LoginResult
}