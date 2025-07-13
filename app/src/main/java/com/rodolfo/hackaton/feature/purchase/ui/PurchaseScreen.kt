package com.rodolfo.hackaton.feature.purchase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodolfo.hackaton.ui.theme.*
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseScreen(
    viewModel: PurchaseViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val purchaseHistory by viewModel.purchaseHistory.collectAsState()
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
                    text = "Historial de Compras",
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
            // Tarjeta de estadísticas del usuario
            UserPurchaseStatsCard(
                userName = userData?.fullName ?: "Usuario",
                totalPurchases = purchaseHistory.size,
                totalPoints = userData?.totalPoints ?: 0
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título de la lista
            Text(
                text = "Tus Pedidos Recientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Lista de compras
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(purchaseHistory) { purchaseItem ->
                        PurchaseHistoryCard(
                            purchaseItem = purchaseItem,
                            onClick = { viewModel.onPurchaseClick(purchaseItem.id) }
                        )
                    }
                }
            }

            // Paginación
            if (totalPages > 1) {
                Spacer(modifier = Modifier.height(16.dp))
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
fun UserPurchaseStatsCard(
    userName: String,
    totalPurchases: Int,
    totalPoints: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Compras de $userName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownText
                )
                Text(
                    text = "$totalPurchases pedidos realizados",
                    fontSize = 14.sp,
                    color = BrownText.copy(alpha = 0.7f)
                )
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryOrange)
            ) {
                Text(
                    text = "$totalPoints pts",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun PurchaseHistoryCard(
    purchaseItem: PurchaseHistoryItem,
    onClick: () -> Unit
) {
    val decimalFormat = DecimalFormat("#.##")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(purchaseItem.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = purchaseItem.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_gallery)
            )

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = purchaseItem.productName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = purchaseItem.productDescription,
                    fontSize = 14.sp,
                    color = BrownText.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getStatusIcon(purchaseItem.status),
                        contentDescription = null,
                        tint = getStatusColor(purchaseItem.status),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = getStatusText(purchaseItem.status),
                        fontSize = 12.sp,
                        color = getStatusColor(purchaseItem.status),
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = purchaseItem.orderDate,
                    fontSize = 12.sp,
                    color = NeutralGray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Precio y puntos
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "S/ ${decimalFormat.format(purchaseItem.totalAmount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownText
                )

                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryOrange.copy(alpha = 0.1f)),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "+${purchaseItem.pointsEarned} pts",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
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
fun getStatusIcon(status: PurchaseStatus): ImageVector {
    return when (status) {
        PurchaseStatus.DELIVERED -> Icons.Default.CheckCircle
        PurchaseStatus.PENDING -> Icons.Default.Schedule
        PurchaseStatus.CANCELLED -> Icons.Default.Cancel
    }
}

@Composable
fun getStatusColor(status: PurchaseStatus): Color {
    return when (status) {
        PurchaseStatus.DELIVERED -> PrimaryOrange
        PurchaseStatus.PENDING -> YellowSoft
        PurchaseStatus.CANCELLED -> ErrorRed
    }
}

fun getStatusText(status: PurchaseStatus): String {
    return when (status) {
        PurchaseStatus.DELIVERED -> "Entregado"
        PurchaseStatus.PENDING -> "Pendiente"
        PurchaseStatus.CANCELLED -> "Cancelado"
    }
}