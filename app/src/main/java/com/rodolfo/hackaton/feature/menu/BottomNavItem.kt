package com.rodolfo.hackaton.feature.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Score
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "home",
        title = "Inicio",
        icon = Icons.Default.Home
    )

    data object Swap : BottomNavItem(
        route = "swap",
        title = "Mis Canjes",
        icon = Icons.Default.SwapVert
    )

    data object Purchase : BottomNavItem(
        route = "purchase",
        title = "Mis Compras",
        icon = Icons.Default.ShoppingCart
    )

    data object Score : BottomNavItem(
        route = "score",
        title = "Mi Puntaje",
        icon = Icons.Default.Score
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Swap,
    BottomNavItem.Purchase,
    BottomNavItem.Score
)