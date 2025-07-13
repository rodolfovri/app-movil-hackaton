package com.rodolfo.hackaton.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val expires_in: Int,
    val user: UserResponse
) {
    @Serializable
    data class UserResponse(
        val id: Int,
        val email: String,
        val full_name: String,
        val is_admin: Boolean,
        val total_points: Int,
    )
}