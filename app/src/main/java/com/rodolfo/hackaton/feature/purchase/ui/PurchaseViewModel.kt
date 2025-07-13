package com.rodolfo.hackaton.feature.purchase.ui

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
class PurchaseViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _purchaseHistory = MutableStateFlow<List<PurchaseHistoryItem>>(emptyList())
    val purchaseHistory: StateFlow<List<PurchaseHistoryItem>> = _purchaseHistory.asStateFlow()

    private val _selectedPurchase = MutableStateFlow<PurchaseHistoryItem?>(null)
    val selectedPurchase: StateFlow<PurchaseHistoryItem?> = _selectedPurchase.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val itemsPerPage = 10
    private val allPurchaseHistory = mutableListOf<PurchaseHistoryItem>()

    init {
        loadUserData()
        loadPurchaseHistory()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            preferencesManager.userData.collect { userData ->
                _userData.value = userData
            }
        }
    }

    fun getPurchaseDetail(purchaseId: Int) {
        viewModelScope.launch {
            val purchase = allPurchaseHistory.find { it.id == purchaseId }
            _selectedPurchase.value = purchase
        }
    }

    fun onPurchaseClick(purchaseId: Int) {
        getPurchaseDetail(purchaseId)
    }

    private fun loadPurchaseHistory() {
        viewModelScope.launch {
            _isLoading.value = true

            // Datos de ejemplo (aquí conectarías con tu API)
            allPurchaseHistory.clear()
            allPurchaseHistory.addAll(
                listOf(
                    PurchaseHistoryItem(
                        id = 1,
                        productName = "Hamburguesa Clásica",
                        productDescription = "Carne de res, lechuga, tomate, cebolla",
                        orderDate = "2024-01-15",
                        deliveryDate = "2024-01-15",
                        totalAmount = 22.90,
                        pointsEarned = 26,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&h=300&fit=crop",
                        deliveryAddress = "Jr. Lima 123, Cercado de Lima",
                        orderTime = "12:30 PM",
                        deliveryTime = "1:00 PM",
                        restaurant = "Hamburguesas del Barrio",
                        paymentMethod = "Tarjeta de Crédito",
                        orderNumber = "ORD123456"
                        ),
                    PurchaseHistoryItem(
                        id = 2,
                        productName = "Pizza Margarita",
                        productDescription = "Masa artesanal, mozzarella, albahaca fresca",
                        orderDate = "2024-01-12",
                        deliveryDate = "2024-01-12",
                        totalAmount = 28.50,
                        pointsEarned = 33,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. Arequipa 456, Miraflores",
                        orderTime = "7:15 PM",
                        deliveryTime = "7:45 PM",
                        restaurant = "Pizzería La Tradición",
                        paymentMethod = "Efectivo",
                        orderNumber = "ORD654321"
                    ),
                    PurchaseHistoryItem(
                        id = 3,
                        productName = "Tacos al Pastor",
                        productDescription = "3 tacos con carne al pastor, piña, cebolla",
                        orderDate = "2024-01-10",
                        deliveryDate = "2024-01-10",
                        totalAmount = 15.75,
                        pointsEarned = 19,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1565299507177-b0ac66763828?w=400&h=300&fit=crop",
                        deliveryAddress = "Calle Los Olivos 789, San Isidro",
                        orderTime = "1:00 PM",
                        deliveryTime = "1:30 PM",
                        restaurant = "Tacos y Más",
                        paymentMethod = "Tarjeta de Débito",
                        orderNumber = "ORD789012",
                    ),
                    PurchaseHistoryItem(
                        id = 4,
                        productName = "Ensalada César",
                        productDescription = "Lechuga romana, pollo, crutones, parmesano",
                        orderDate = "2024-01-08",
                        deliveryDate = "2024-01-08",
                        totalAmount = 18.00,
                        pointsEarned = 22,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. Pardo 123, Miraflores",
                        orderTime = "12:00 PM",
                        deliveryTime = "12:30 PM",
                        restaurant = "Restaurante Saludable",
                        paymentMethod = "Efectivo",
                        orderNumber = "ORD321654"
                    ),
                    PurchaseHistoryItem(
                        id = 5,
                        productName = "Sushi Combo",
                        productDescription = "15 piezas variadas de sushi y maki",
                        orderDate = "2024-01-05",
                        deliveryDate = "2024-01-05",
                        totalAmount = 42.00,
                        pointsEarned = 45,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. Pardo 789, Miraflores",
                        orderTime = "8:30 PM",
                        deliveryTime = "9:00 PM",
                        restaurant = "Sushi House",
                        paymentMethod = "Tarjeta de Crédito",
                        orderNumber = "ORD456789"
                    ),
                    PurchaseHistoryItem(
                        id = 6,
                        productName = "Café Americano",
                        productDescription = "Café premium con leche de almendras",
                        orderDate = "2024-01-03",
                        deliveryDate = "2024-01-03",
                        totalAmount = 7.50,
                        pointsEarned = 9,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=300&fit=crop",
                        deliveryAddress = "Calle Los Cedros 123, San Isidro",
                        orderTime = "8:00 AM",
                        deliveryTime = "8:30 AM",
                        restaurant = "Cafetería Aromas",
                        paymentMethod = "Efectivo",
                        orderNumber = "ORD123456"
                    ),
                    PurchaseHistoryItem(
                        id = 7,
                        productName = "Pasta Carbonara",
                        productDescription = "Pasta con panceta, huevo, parmesano",
                        orderDate = "2024-01-01",
                        deliveryDate = "2024-01-01",
                        totalAmount = 25.75,
                        pointsEarned = 29,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1621996346565-e3dbc353d2e5?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. José Larco 123, Miraflores",
                        orderTime = "6:00 PM",
                        deliveryTime = "6:30 PM",
                        restaurant = "Restaurante La Bella Italia",
                        paymentMethod = "Tarjeta de Crédito",
                        orderNumber = "ORD321654"
                    ),
                    PurchaseHistoryItem(
                        id = 8,
                        productName = "Smoothie Tropical",
                        productDescription = "Mango, piña, plátano, yogurt griego",
                        orderDate = "2023-12-28",
                        deliveryDate = "2023-12-28",
                        totalAmount = 11.90,
                        pointsEarned = 13,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=400&h=300&fit=crop",
                        deliveryAddress = "Calle Los Álamos 456, San Borja",
                        orderTime = "3:00 PM",
                        deliveryTime = "3:30 PM",
                        restaurant = "Smoothie Bar Tropical",
                        paymentMethod = "Efectivo",
                        orderNumber = "ORD987654"
                    ),
                    PurchaseHistoryItem(
                        id = 9,
                        productName = "Pollo a la Parrilla",
                        productDescription = "Pechuga de pollo con vegetales asados",
                        orderDate = "2023-12-25",
                        deliveryDate = "2023-12-25",
                        totalAmount = 32.25,
                        pointsEarned = 35,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1598103442097-8b74394b95c6?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. Pardo 123, Miraflores",
                        orderTime = "1:00 PM",
                        deliveryTime = "1:30 PM",
                        restaurant = "Restaurante Gourmet",
                        paymentMethod = "Tarjeta de Crédito",
                        orderNumber = "ORD123789"
                    ),
                    PurchaseHistoryItem(
                        id = 10,
                        productName = "Burrito Mexicano",
                        productDescription = "Pollo, frijoles, arroz, guacamole",
                        orderDate = "2023-12-22",
                        deliveryDate = "2023-12-22",
                        totalAmount = 17.50,
                        pointsEarned = 20,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1626700051175-6818013e1d4f?w=400&h=300&fit=crop",
                        deliveryAddress = "Calle Los Abetos 321, San Borja",
                        orderTime = "6:30 PM",
                        deliveryTime = "7:00 PM",
                        restaurant = "Restaurante Mexicano El Camino",
                        paymentMethod = "Tarjeta de Débito",
                        orderNumber = "ORD456789"
                    ),
                    PurchaseHistoryItem(
                        id = 11,
                        productName = "Helado Artesanal",
                        productDescription = "Helado de vainilla con chocolate",
                        orderDate = "2023-12-20",
                        deliveryDate = "2023-12-20",
                        totalAmount = 13.75,
                        pointsEarned = 16,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1560008581-09826d1de69e?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. José Larco 456, Miraflores",
                        orderTime = "5:00 PM",
                        deliveryTime = "5:30 PM",
                        restaurant = "Heladería Dulce Sabor",
                        paymentMethod = "Efectivo",
                        orderNumber = "ORD987321"
                    ),
                    PurchaseHistoryItem(
                        id = 12,
                        productName = "Sandwich Club",
                        productDescription = "Pollo, tocino, lechuga, tomate, mayo",
                        orderDate = "2023-12-18",
                        deliveryDate = "2023-12-18",
                        totalAmount = 21.00,
                        pointsEarned = 24,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1567229569165-91ac2f32aab1?w=400&h=300&fit=crop",
                        deliveryAddress = "Calle Los Jazmines 123, Miraflores",
                        orderTime = "2:00 PM",
                        deliveryTime = "2:30 PM",
                        restaurant = "Restaurante Gourmet",
                        paymentMethod = "Tarjeta de Crédito",
                        orderNumber = "ORD321654"
                    ),
                    PurchaseHistoryItem(
                        id = 13,
                        productName = "Quesadilla de Pollo",
                        productDescription = "Tortilla con pollo, queso, pimientos",
                        orderDate = "2023-12-15",
                        deliveryDate = "2023-12-15",
                        totalAmount = 14.25,
                        pointsEarned = 16,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1593759608743-75d9c3ba8c04?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. Javier Prado 789, San Isidro",
                        orderTime = "1:30 PM",
                        deliveryTime = "2:00 PM",
                        restaurant = "Restaurante Mexicano El Camino",
                        paymentMethod = "Tarjeta de Débito",
                        orderNumber = "ORD456123"
                    ),
                    PurchaseHistoryItem(
                        id = 14,
                        productName = "Lasagna Bolognesa",
                        productDescription = "Pasta con carne, bechamel, mozzarella",
                        orderDate = "2023-12-12",
                        deliveryDate = "2023-12-12",
                        totalAmount = 35.90,
                        pointsEarned = 39,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1599479575503-2ac2c6b7b5b2?w=400&h=300&fit=crop",
                        deliveryAddress = "Calle Los Pinos 456, San Borja",
                        orderTime = "6:00 PM",
                        deliveryTime = "6:30 PM",
                        restaurant = "Restaurante La Bella Italia",
                        paymentMethod = "Efectivo",
                        orderNumber = "ORD123789"
                    ),
                    PurchaseHistoryItem(
                        id = 15,
                        productName = "Té Chai Latte",
                        productDescription = "Té chai con leche de coco y especias",
                        orderDate = "2023-12-10",
                        deliveryDate = "2023-12-10",
                        totalAmount = 8.75,
                        pointsEarned = 10,
                        status = PurchaseStatus.DELIVERED,
                        imageUrl = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=300&fit=crop",
                        deliveryAddress = "Av. Pardo 123, Miraflores",
                        orderTime = "3:30 PM",
                        deliveryTime = "4:00 PM",
                        restaurant = "Cafetería Aromas",
                        paymentMethod = "Tarjeta de Crédito",
                        orderNumber = "ORD987654"
                    )
                )
            )

            _totalPages.value = kotlin.math.ceil(allPurchaseHistory.size.toDouble() / itemsPerPage).toInt()
            updateCurrentPageData()
            _isLoading.value = false
        }
    }

    private fun updateCurrentPageData() {
        val startIndex = (_currentPage.value - 1) * itemsPerPage
        val endIndex = kotlin.math.min(startIndex + itemsPerPage, allPurchaseHistory.size)

        _purchaseHistory.value = if (startIndex < allPurchaseHistory.size) {
            allPurchaseHistory.subList(startIndex, endIndex)
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

data class PurchaseHistoryItem(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val orderDate: String,
    val deliveryDate: String,
    val totalAmount: Double,
    val pointsEarned: Int,
    val status: PurchaseStatus,
    val imageUrl: String,
    val deliveryAddress: String,
    val orderTime: String,
    val deliveryTime: String,
    val restaurant: String,
    val paymentMethod: String,
    val orderNumber: String
)

enum class PurchaseStatus {
    DELIVERED,
    PENDING,
    CANCELLED
}