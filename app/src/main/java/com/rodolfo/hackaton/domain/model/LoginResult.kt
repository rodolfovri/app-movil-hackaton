package com.rodolfo.hackaton.domain.model

data class LoginResult (
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val expires_in: Int,
    val user: UserResult
) {
    data class UserResult(
        val id: Int,
        val email: String,
        val full_name: String,
        val is_admin: Boolean,
        val total_points: Int
    )
}