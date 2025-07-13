package com.rodolfo.hackaton.feature.score.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodolfo.hackaton.data.local.PreferencesManager
import com.rodolfo.hackaton.data.local.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _userStats = MutableStateFlow<UserStats?>(null)
    val userStats: StateFlow<UserStats?> = _userStats.asStateFlow()

    private val _recycleLevel = MutableStateFlow<RecycleLevel?>(null)
    val recycleLevel: StateFlow<RecycleLevel?> = _recycleLevel.asStateFlow()

    private val _recentActivities = MutableStateFlow<List<RecentActivity>>(emptyList())
    val recentActivities: StateFlow<List<RecentActivity>> = _recentActivities.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserData()
        loadUserStats()
        loadRecentActivities()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            preferencesManager.userData.collect { userData ->
                _userData.value = userData
                calculateRecycleLevel(userData?.totalPoints ?: 0)
            }
        }
    }

    private fun loadUserStats() {
        viewModelScope.launch {
            _isLoading.value = true

            // Datos de ejemplo (aquí conectarías con tu API)
            _userStats.value = UserStats(
                totalPurchases = 15,
                totalSwaps = 8,
                totalPointsEarned = 1850,
                totalPointsSpent = 650,
                averageOrderValue = 22.50,
                recycleStreak = 12,
                monthlyPoints = 320,
                weeklyPoints = 85
            )

            _isLoading.value = false
        }
    }

    private fun loadRecentActivities() {
        viewModelScope.launch {
            _recentActivities.value = listOf(
                RecentActivity(
                    id = 1,
                    type = ActivityType.PURCHASE,
                    description = "Compra: Hamburguesa Clásica",
                    points = 26,
                    date = "2024-01-15"
                ),
                RecentActivity(
                    id = 2,
                    type = ActivityType.SWAP,
                    description = "Canje: Descuento 10%",
                    points = -50,
                    date = "2024-01-14"
                ),
                RecentActivity(
                    id = 3,
                    type = ActivityType.PURCHASE,
                    description = "Compra: Pizza Margarita",
                    points = 33,
                    date = "2024-01-12"
                ),
                RecentActivity(
                    id = 4,
                    type = ActivityType.SWAP,
                    description = "Canje: Envío Gratis",
                    points = -25,
                    date = "2024-01-11"
                ),
                RecentActivity(
                    id = 5,
                    type = ActivityType.PURCHASE,
                    description = "Compra: Tacos al Pastor",
                    points = 19,
                    date = "2024-01-10"
                ),
                RecentActivity(
                    id = 6,
                    type = ActivityType.BONUS,
                    description = "Bonus: Reciclaje Continuo",
                    points = 50,
                    date = "2024-01-09"
                ),
                RecentActivity(
                    id = 7,
                    type = ActivityType.PURCHASE,
                    description = "Compra: Ensalada César",
                    points = 22,
                    date = "2024-01-08"
                ),
                RecentActivity(
                    id = 8,
                    type = ActivityType.SWAP,
                    description = "Canje: Cupón $5",
                    points = -100,
                    date = "2024-01-07"
                )
            )
        }
    }

    private fun calculateRecycleLevel(totalPoints: Int) {
        _recycleLevel.value = when {
            totalPoints >= 2000 -> RecycleLevel(
                name = "Eco Master",
                icon = "🌟",
                color = "#FFD700",
                description = "Reciclador experto",
                pointsRequired = 2000,
                nextLevelPoints = null,
                benefits = listOf("20% descuento adicional", "Envío gratis premium", "Soporte prioritario")
            )
            totalPoints >= 1500 -> RecycleLevel(
                name = "Eco Expert",
                icon = "🏆",
                color = "#FF8000",
                description = "Reciclador avanzado",
                pointsRequired = 1500,
                nextLevelPoints = 2000,
                benefits = listOf("15% descuento adicional", "Canjes exclusivos", "Puntos dobles")
            )
            totalPoints >= 1000 -> RecycleLevel(
                name = "Eco Champion",
                icon = "🥇",
                color = "#4B0082",
                description = "Reciclador comprometido",
                pointsRequired = 1000,
                nextLevelPoints = 1500,
                benefits = listOf("10% descuento adicional", "Envío gratis", "Ofertas especiales")
            )
            totalPoints >= 500 -> RecycleLevel(
                name = "Eco Friend",
                icon = "🌱",
                color = "#32CD32",
                description = "Reciclador activo",
                pointsRequired = 500,
                nextLevelPoints = 1000,
                benefits = listOf("5% descuento adicional", "Canjes básicos", "Alertas de ofertas")
            )
            else -> RecycleLevel(
                name = "Eco Beginner",
                icon = "🌿",
                color = "#90EE90",
                description = "Nuevo reciclador",
                pointsRequired = 0,
                nextLevelPoints = 500,
                benefits = listOf("Bienvenida", "Puntos básicos", "Acceso a canjes")
            )
        }
    }
}

data class UserStats(
    val totalPurchases: Int,
    val totalSwaps: Int,
    val totalPointsEarned: Int,
    val totalPointsSpent: Int,
    val averageOrderValue: Double,
    val recycleStreak: Int,
    val monthlyPoints: Int,
    val weeklyPoints: Int
)

data class RecycleLevel(
    val name: String,
    val icon: String,
    val color: String,
    val description: String,
    val pointsRequired: Int,
    val nextLevelPoints: Int?,
    val benefits: List<String>
)

data class RecentActivity(
    val id: Int,
    val type: ActivityType,
    val description: String,
    val points: Int,
    val date: String
)

enum class ActivityType {
    PURCHASE,
    SWAP,
    BONUS
}