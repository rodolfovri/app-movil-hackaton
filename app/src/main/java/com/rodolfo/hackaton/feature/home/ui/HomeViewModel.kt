package com.rodolfo.hackaton.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector
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
class HomeViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    private val _promotions = MutableStateFlow<List<Promotion>>(emptyList())
    val promotions: StateFlow<List<Promotion>> = _promotions.asStateFlow()

    init {
        loadUserData()
        loadRewards()
        loadPromotions()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            preferencesManager.userData.collect { userData ->
                _userData.value = userData
            }
        }
    }

    private fun loadRewards() {
        _rewards.value = listOf(
            Reward(1, "Descuento 10%", Icons.Default.Percent, 100, true),
            Reward(2, "Envío Gratis", Icons.Default.LocalShipping, 50, true),
            Reward(3, "Cupón $5", Icons.Default.MonetizationOn, 200, true),
            Reward(4, "Descuento 15%", Icons.Default.Percent, 300, true),
            Reward(5, "Puntos Extra", Icons.Default.Star, 150, true),
            Reward(6, "Descuento VIP", Icons.Default.Store, 500, true)
        )
    }

    private fun loadPromotions() {
        _promotions.value = listOf(
            Promotion(
                id = 1,
                title = "Oferta Especial",
                description = "50% de descuento en productos seleccionados",
                imageUrl = "https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=400&h=300&fit=crop&crop=center"
            ),
            Promotion(
                id = 2,
                title = "Envío Gratis",
                description = "En compras mayores a $30",
                imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop&crop=center"
            ),
            Promotion(
                id = 3,
                title = "2x1 en Bebidas",
                description = "Válido hasta fin de mes",
                imageUrl = "https://images.unsplash.com/photo-1544145945-f90425340c7e?w=400&h=300&fit=crop&crop=center"
            ),
            Promotion(
                id = 4,
                title = "Cashback 20%",
                description = "En tu primera compra",
                imageUrl = "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400&h=300&fit=crop&crop=center"
            ),
            Promotion(
                id = 5,
                title = "Descuento VIP",
                description = "Exclusivo para miembros premium",
                imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=400&h=300&fit=crop&crop=center"
            )
        )
    }

    fun onRewardClick(rewardId: Int) {
        println("Recompensa clickeada: $rewardId")
    }
}

data class Reward(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val pointsRequired: Int,
    val isAvailable: Boolean
)

data class Promotion(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)