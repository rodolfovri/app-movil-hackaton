package com.rodolfo.hackaton.feature.login.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodolfo.hackaton.data.local.PreferencesManager
import com.rodolfo.hackaton.data.remote.dto.request.LoginRequest
import com.rodolfo.hackaton.domain.model.LoginResult
import com.rodolfo.hackaton.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun login(email: String, password: String) {
        _loginUiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(
                    email = email,
                    password = password
                )
                val result = authRepository.login(request)
                preferencesManager.saveLoginResult(result)

                _loginUiState.value = LoginUiState.Success(
                    "Bienvenido ${result.user.full_name}",
                    result
                )
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun clearError() {
        _loginUiState.value = LoginUiState.Initial
    }

    sealed class LoginUiState {
        data object Initial : LoginUiState()
        data object Loading : LoginUiState()
        data class Success(val message: String, val loginResult: LoginResult) : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }
}