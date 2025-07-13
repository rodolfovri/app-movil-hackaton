package com.rodolfo.hackaton.data.repository

import com.rodolfo.hackaton.data.remote.api.ApiService
import com.rodolfo.hackaton.data.remote.dto.request.LoginRequest
import com.rodolfo.hackaton.domain.model.LoginResult
import com.rodolfo.hackaton.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService
): AuthRepository {

    override suspend fun login(request: LoginRequest): LoginResult {
        val response = apiService.login(request)
        return LoginResult(
            access_token = response.access_token,
            refresh_token = response.refresh_token,
            token_type = response.token_type,
            expires_in = response.expires_in,
            user = LoginResult.UserResult(
                id = response.user.id,
                email = response.user.email,
                full_name = response.user.full_name,
                is_admin = response.user.is_admin,
                total_points = response.user.total_points
            )
        )
    }
}