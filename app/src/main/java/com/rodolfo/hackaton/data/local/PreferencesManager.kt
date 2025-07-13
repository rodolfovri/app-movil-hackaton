package com.rodolfo.hackaton.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.rodolfo.hackaton.domain.model.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // Definir claves para preferencias
    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_FULL_NAME = stringPreferencesKey("user_full_name")
        val USER_IS_ADMIN = booleanPreferencesKey("user_is_admin")
        val USER_TOTAL_POINTS = intPreferencesKey("user_total_points")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val TOKEN_TYPE = stringPreferencesKey("token_type")
        val EXPIRES_IN = intPreferencesKey("expires_in")
    }

    // StateFlow observable para datos de usuario
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    // StateFlow observable para token de autenticación
    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken

    // Cargar datos almacenados
    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.collect { preferences ->
                val userId = preferences[PreferencesKeys.USER_ID]
                val userEmail = preferences[PreferencesKeys.USER_EMAIL]
                val userFullName = preferences[PreferencesKeys.USER_FULL_NAME]
                val userIsAdmin = preferences[PreferencesKeys.USER_IS_ADMIN]
                val userTotalPoints = preferences[PreferencesKeys.USER_TOTAL_POINTS]
                val accessToken = preferences[PreferencesKeys.ACCESS_TOKEN]
                val refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN]
                val tokenType = preferences[PreferencesKeys.TOKEN_TYPE]
                val expiresIn = preferences[PreferencesKeys.EXPIRES_IN]

                if (userId != null && userEmail != null && userFullName != null) {
                    _userData.value = UserData(
                        id = userId,
                        email = userEmail,
                        fullName = userFullName,
                        isAdmin = userIsAdmin ?: false,
                        totalPoints = userTotalPoints ?: 0,
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        tokenType = tokenType,
                        expiresIn = expiresIn
                    )
                }

                _authToken.value = accessToken
            }
        }
    }

    // Guardar datos de login
    suspend fun saveLoginResult(loginResult: LoginResult) {
        dataStore.edit { preferences ->
            // Guardar datos del usuario
            preferences[PreferencesKeys.USER_ID] = loginResult.user.id
            preferences[PreferencesKeys.USER_EMAIL] = loginResult.user.email
            preferences[PreferencesKeys.USER_FULL_NAME] = loginResult.user.full_name
            preferences[PreferencesKeys.USER_IS_ADMIN] = loginResult.user.is_admin
            preferences[PreferencesKeys.USER_TOTAL_POINTS] = loginResult.user.total_points

            // Guardar tokens de autenticación
            preferences[PreferencesKeys.ACCESS_TOKEN] = loginResult.access_token
            preferences[PreferencesKeys.REFRESH_TOKEN] = loginResult.refresh_token
            preferences[PreferencesKeys.TOKEN_TYPE] = loginResult.token_type
            preferences[PreferencesKeys.EXPIRES_IN] = loginResult.expires_in
        }

        // Actualizar StateFlow
        _userData.value = UserData(
            id = loginResult.user.id,
            email = loginResult.user.email,
            fullName = loginResult.user.full_name,
            isAdmin = loginResult.user.is_admin,
            totalPoints = loginResult.user.total_points,
            accessToken = loginResult.access_token,
            refreshToken = loginResult.refresh_token,
            tokenType = loginResult.token_type,
            expiresIn = loginResult.expires_in
        )

        _authToken.value = loginResult.access_token
    }

    // Obtener token de acceso
    suspend fun getAccessToken(): String? {
        return _authToken.value
    }

    // Verificar si el usuario está logueado
    suspend fun isLoggedIn(): Boolean {
        return _authToken.value != null
    }

    // Actualizar token de acceso
    suspend fun updateAccessToken(newToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = newToken
        }
        _authToken.value = newToken
    }

    // Limpiar datos (logout)
    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        _userData.value = null
        _authToken.value = null
    }

    // Obtener datos del usuario actual
    fun getCurrentUser(): UserData? {
        return _userData.value
    }
}

data class UserData(
    val id: Int,
    val email: String,
    val fullName: String,
    val isAdmin: Boolean,
    val totalPoints: Int,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Int? = null
)