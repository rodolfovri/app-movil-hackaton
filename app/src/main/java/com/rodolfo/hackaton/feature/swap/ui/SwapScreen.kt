package com.rodolfo.hackaton.feature.swap.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodolfo.hackaton.feature.swap.ui.SwapHistoryItem
import com.rodolfo.hackaton.feature.swap.ui.SwapStatus
import com.rodolfo.hackaton.feature.swap.ui.SwapViewModel
import com.rodolfo.hackaton.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwapScreen(
    viewModel: SwapViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val swapHistory by viewModel.swapHistory.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Historial de Canjes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownText
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CreamBackground
            )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título de la lista
            Text(
                text = "Tus Canjes Recientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Lista de canjes
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryOrange,
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(swapHistory) { historyItem ->
                        SwapHistoryCard(historyItem = historyItem)
                    }
                }
            }

            // Paginación
            if (totalPages > 1) {
                PaginationControls(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPreviousClick = { viewModel.previousPage() },
                    onNextClick = { viewModel.nextPage() }
                )
            }
        }
    }
}

@Composable
fun SwapHistoryCard(historyItem: SwapHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del canje
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = historyItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownText
                )
                Text(
                    text = historyItem.description,
                    fontSize = 14.sp,
                    color = BrownText.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = historyItem.date,
                    fontSize = 12.sp,
                    color = NeutralGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Estado y puntos
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = getStatusIcon(historyItem.status),
                        contentDescription = null,
                        tint = getStatusColor(historyItem.status),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = getStatusText(historyItem.status),
                        fontSize = 12.sp,
                        color = getStatusColor(historyItem.status),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "${historyItem.pointsUsed} pts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Anterior
            Button(
                onClick = onPreviousClick,
                enabled = currentPage > 1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryOrange,
                    disabledContainerColor = NeutralGray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Anterior",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Indicador de página
            Text(
                text = "$currentPage de $totalPages",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BrownText,
                textAlign = TextAlign.Center
            )

            // Botón Siguiente
            Button(
                onClick = onNextClick,
                enabled = currentPage < totalPages,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryOrange,
                    disabledContainerColor = NeutralGray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun getStatusIcon(status: SwapStatus): ImageVector {
    return when (status) {
        SwapStatus.ACTIVE -> Icons.Default.Schedule
        SwapStatus.USED -> Icons.Default.CheckCircle
        SwapStatus.EXPIRED -> Icons.Default.Error
    }
}

@Composable
fun getStatusColor(status: SwapStatus): Color {
    return when (status) {
        SwapStatus.ACTIVE -> YellowSoft
        SwapStatus.USED -> PrimaryOrange
        SwapStatus.EXPIRED -> ErrorRed
    }
}

fun getStatusText(status: SwapStatus): String {
    return when (status) {
        SwapStatus.ACTIVE -> "Activo"
        SwapStatus.USED -> "Usado"
        SwapStatus.EXPIRED -> "Expirado"
    }
}