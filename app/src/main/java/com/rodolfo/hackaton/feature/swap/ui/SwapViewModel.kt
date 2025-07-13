package com.rodolfo.hackaton.feature.swap.ui

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
class SwapViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _swapHistory = MutableStateFlow<List<SwapHistoryItem>>(emptyList())
    val swapHistory: StateFlow<List<SwapHistoryItem>> = _swapHistory.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val itemsPerPage = 10
    private val allSwapHistory = mutableListOf<SwapHistoryItem>()

    init {
        loadUserData()
        loadSwapHistory()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            preferencesManager.userData.collect { userData ->
                _userData.value = userData
            }
        }
    }

    private fun loadSwapHistory() {
        viewModelScope.launch {
            _isLoading.value = true

            // Datos de ejemplo (aquí conectarías con tu API)
            allSwapHistory.clear()
            allSwapHistory.addAll(
                listOf(
                    SwapHistoryItem(1, "Descuento 10%", "Usado en Restaurante ABC", "2024-01-15", 100, SwapStatus.USED),
                    SwapHistoryItem(2, "Envío Gratis", "Aplicado en pedido #123", "2024-01-14", 50, SwapStatus.USED),
                    SwapHistoryItem(3, "Cupón $5", "Válido hasta el 31/01", "2024-01-13", 200, SwapStatus.ACTIVE),
                    SwapHistoryItem(4, "Descuento 15%", "Usado en Tienda XYZ", "2024-01-12", 300, SwapStatus.USED),
                    SwapHistoryItem(5, "Puntos Extra", "Aplicado en cuenta", "2024-01-11", 150, SwapStatus.USED),
                    SwapHistoryItem(6, "Descuento VIP", "Válido hasta el 28/02", "2024-01-10", 500, SwapStatus.ACTIVE),
                    SwapHistoryItem(7, "Cupón $10", "Usado en Farmacia DEF", "2024-01-09", 400, SwapStatus.USED),
                    SwapHistoryItem(8, "Envío Express", "Aplicado en pedido #456", "2024-01-08", 75, SwapStatus.USED),
                    SwapHistoryItem(9, "Descuento 20%", "Expirado", "2024-01-07", 350, SwapStatus.EXPIRED),
                    SwapHistoryItem(10, "Puntos Dobles", "Aplicado en cuenta", "2024-01-06", 200, SwapStatus.USED),
                    SwapHistoryItem(11, "Cupón $15", "Válido hasta el 15/02", "2024-01-05", 600, SwapStatus.ACTIVE),
                    SwapHistoryItem(12, "Descuento 25%", "Usado en Supermercado GHI", "2024-01-04", 450, SwapStatus.USED),
                    SwapHistoryItem(13, "Envío Gratis", "Aplicado en pedido #789", "2024-01-03", 50, SwapStatus.USED),
                    SwapHistoryItem(14, "Cupón $20", "Expirado", "2024-01-02", 800, SwapStatus.EXPIRED),
                    SwapHistoryItem(15, "Descuento 30%", "Usado en Restaurante JKL", "2024-01-01", 600, SwapStatus.USED)
                )
            )

            _totalPages.value = kotlin.math.ceil(allSwapHistory.size.toDouble() / itemsPerPage).toInt()
            updateCurrentPageData()
            _isLoading.value = false
        }
    }

    private fun updateCurrentPageData() {
        val startIndex = (_currentPage.value - 1) * itemsPerPage
        val endIndex = kotlin.math.min(startIndex + itemsPerPage, allSwapHistory.size)

        _swapHistory.value = if (startIndex < allSwapHistory.size) {
            allSwapHistory.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
    }

    fun nextPage() {
        if (_currentPage.value < _totalPages.value) {
            _currentPage.value += 1
            updateCurrentPageData()
        }
    }

    fun previousPage() {
        if (_currentPage.value > 1) {
            _currentPage.value -= 1
            updateCurrentPageData()
        }
    }

    fun goToPage(page: Int) {
        if (page in 1.._totalPages.value) {
            _currentPage.value = page
            updateCurrentPageData()
        }
    }
}

data class SwapHistoryItem(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val pointsUsed: Int,
    val status: SwapStatus
)

enum class SwapStatus {
    ACTIVE,
    USED,
    EXPIRED
}