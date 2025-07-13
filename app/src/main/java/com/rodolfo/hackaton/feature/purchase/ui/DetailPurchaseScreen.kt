package com.rodolfo.hackaton.feature.purchase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rodolfo.hackaton.ui.theme.*
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPurchaseScreen(
    onBackClick: () -> Unit,
    viewModel: PurchaseViewModel = hiltViewModel()
) {
    val selectedPurchase by viewModel.selectedPurchase.collectAsState()
    val decimalFormat = DecimalFormat("#.##")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Detalle de Compra",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownText
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = BrownText
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CreamBackground
            )
        )

        selectedPurchase?.let { purchase ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Imagen y información principal del producto
                ProductMainInfoCard(purchase = purchase)

                // Información del pedido
                OrderDetailsCard(purchase = purchase)

                // Información de entrega
                DeliveryDetailsCard(purchase = purchase)

                // Información de pago y puntos
                PaymentDetailsCard(purchase = purchase)

                // Estado del pedido
                OrderStatusCard(purchase = purchase)
            }
        } ?: run {
            // Estado de carga o error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se pudo cargar el detalle de la compra",
                    fontSize = 16.sp,
                    color = BrownText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ProductMainInfoCard(purchase: PurchaseHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(purchase.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = purchase.productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_gallery)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del producto
            Text(
                text = purchase.productName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText
            )

            // Descripción
            Text(
                text = purchase.productDescription,
                fontSize = 16.sp,
                color = BrownText.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )

            // Restaurante
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = PrimaryOrange,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = purchase.restaurant,
                    fontSize = 14.sp,
                    color = BrownText,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun OrderDetailsCard(purchase: PurchaseHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Información del Pedido",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DetailRow(
                icon = Icons.Default.Receipt,
                label = "Número de Orden",
                value = purchase.orderNumber
            )

            DetailRow(
                icon = Icons.Default.AccessTime,
                label = "Fecha de Pedido",
                value = "${purchase.orderDate} - ${purchase.orderTime}"
            )

            DetailRow(
                icon = Icons.Default.Payment,
                label = "Método de Pago",
                value = purchase.paymentMethod
            )
        }
    }
}

@Composable
fun DeliveryDetailsCard(purchase: PurchaseHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Información de Entrega",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DetailRow(
                icon = Icons.Default.LocationOn,
                label = "Dirección de Entrega",
                value = purchase.deliveryAddress
            )

            if (purchase.status == PurchaseStatus.DELIVERED) {
                DetailRow(
                    icon = Icons.Default.AccessTime,
                    label = "Fecha de Entrega",
                    value = "${purchase.deliveryDate} - ${purchase.deliveryTime}"
                )
            }
        }
    }
}

@Composable
fun PaymentDetailsCard(purchase: PurchaseHistoryItem) {
    val decimalFormat = DecimalFormat("#.##")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Resumen de Pago",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Precio total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Pagado",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = BrownText
                )
                Text(
                    text = "S/ ${decimalFormat.format(purchase.totalAmount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = NeutralGray.copy(alpha = 0.3f)
            )

            // Puntos ganados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Puntos Ganados",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = BrownText
                )
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryOrange)
                ) {
                    Text(
                        text = "+${purchase.pointsEarned} pts",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderStatusCard(purchase: PurchaseHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Estado del Pedido",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = getStatusIcon(purchase.status),
                    contentDescription = null,
                    tint = getStatusColor(purchase.status),
                    modifier = Modifier.size(32.dp)
                )

                Column {
                    Text(
                        text = getStatusText(purchase.status),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(purchase.status)
                    )
                    Text(
                        text = getStatusDescription(purchase.status),
                        fontSize = 14.sp,
                        color = BrownText.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryOrange,
            modifier = Modifier.size(20.dp)
        )

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = BrownText.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BrownText
            )
        }
    }
}

fun getStatusDescription(status: PurchaseStatus): String {
    return when (status) {
        PurchaseStatus.DELIVERED -> "Tu pedido ha sido entregado exitosamente"
        PurchaseStatus.PENDING -> "Tu pedido está siendo preparado"
        PurchaseStatus.CANCELLED -> "Tu pedido ha sido cancelado"
    }
}