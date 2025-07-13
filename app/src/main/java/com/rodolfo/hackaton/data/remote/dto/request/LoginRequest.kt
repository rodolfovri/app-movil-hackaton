package com.rodolfo.hackaton.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val email: String,
    val password: String
)