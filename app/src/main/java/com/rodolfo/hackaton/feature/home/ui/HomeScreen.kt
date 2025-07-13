package com.rodolfo.hackaton.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val rewards by viewModel.rewards.collectAsState()
    val promotions by viewModel.promotions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        // Top App Bar personalizada
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icono de usuario
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Saludo con nombre
                    Column {
                        Text(
                            text = "Hola,",
                            fontSize = 14.sp,
                            color = BrownText.copy(alpha = 0.7f)
                        )
                        Text(
                            text = userData?.fullName ?: "Usuario",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrownText
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CreamBackground
            )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Tarjeta de puntos
            PointsCard(totalPoints = userData?.totalPoints ?: 0)

            // Sección de recompensas
            RewardsSection(
                rewards = rewards,
                onRewardClick = { rewardId ->
                    viewModel.onRewardClick(rewardId)
                }
            )

            // Sección de promociones
            PromotionsSection(promotions = promotions)
        }
    }
}

@Composable
fun PointsCard(totalPoints: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PrimaryOrange, OrangeLight)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Puntos",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tus Puntos",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = totalPoints.toString(),
                    fontSize = 36.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RewardsSection(
    rewards: List<Reward>,
    onRewardClick: (Int) -> Unit
) {
    Column {
        Text(
            text = "Tus Recompensas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BrownText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp) // Altura fija para 2 filas
        ) {
            items(rewards) { reward ->
                RewardIconCard(
                    reward = reward,
                    onClick = { onRewardClick(reward.id) }
                )
            }
        }
    }
}

// Recompensa individual
@Composable
fun RewardIconCard(
    reward: Reward,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reward.isAvailable) Color.White else NeutralGray.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (reward.isAvailable) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (reward.isAvailable)
                            PrimaryOrange.copy(alpha = 0.1f)
                        else
                            NeutralGray.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = reward.icon,
                    contentDescription = reward.title,
                    tint = if (reward.isAvailable) PrimaryOrange else NeutralGray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Título
            Text(
                text = reward.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (reward.isAvailable) BrownText else NeutralGray,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            // Puntos requeridos
            Text(
                text = "${reward.pointsRequired} pts",
                fontSize = 10.sp,
                color = if (reward.isAvailable) PrimaryOrange else NeutralGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PromotionsSection(promotions: List<Promotion>) {
    Column {
        Text(
            text = "Promociones Especiales",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BrownText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(promotions) { promotion ->
                PromotionCard(promotion = promotion)
            }
        }
    }
}

@Composable
fun PromotionCard(promotion: Promotion) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de fondo
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(promotion.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = promotion.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_gallery)
            )

            // Overlay con gradiente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Contenido de texto
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Espacio para que el texto vaya abajo
                Spacer(modifier = Modifier.weight(1f))

                // Contenido de texto
                Column {
                    Text(
                        text = promotion.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = promotion.description,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Botón "Ver más"
                    Button(
                        onClick = { /* Acción de la promoción */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryOrange
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(32.dp)
                    ) {
                        Text(
                            text = "Ver más",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}