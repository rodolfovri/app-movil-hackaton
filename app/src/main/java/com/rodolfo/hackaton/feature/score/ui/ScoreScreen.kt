package com.rodolfo.hackaton.feature.score.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodolfo.hackaton.ui.theme.*
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    viewModel: ScoreViewModel = hiltViewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val userStats by viewModel.userStats.collectAsState()
    val recycleLevel by viewModel.recycleLevel.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()
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
                    text = "Mi Puntaje",
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
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = PrimaryOrange,
                    modifier = Modifier.size(48.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta de puntos principales
                MainScoreCard(
                    currentPoints = userData?.totalPoints ?: 0,
                    userName = userData?.fullName ?: "Usuario"
                )

                // Tarjeta de nivel de reciclaje
                recycleLevel?.let { level ->
                    RecycleLevelCard(level = level, currentPoints = userData?.totalPoints ?: 0)
                }

                // Estadísticas generales
                userStats?.let { stats ->
                    StatsGrid(stats = stats)
                }

                // Actividades recientes
                RecentActivitiesSection(activities = recentActivities)
            }
        }
    }
}

@Composable
fun MainScoreCard(
    currentPoints: Int,
    userName: String
) {
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
        ) {
            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Puntos de $userName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Text(
                    text = "$currentPoints",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Puntos Disponibles",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun RecycleLevelCard(
    level: RecycleLevel,
    currentPoints: Int
) {
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
            // Título de sección
            Text(
                text = "Nivel de Reciclaje",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Nivel actual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = level.icon,
                        fontSize = 32.sp
                    )
                    Column {
                        Text(
                            text = level.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrownText
                        )
                        Text(
                            text = level.description,
                            fontSize = 14.sp,
                            color = BrownText.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Progreso al siguiente nivel
            level.nextLevelPoints?.let { nextLevel ->
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(
                        text = "Progreso al siguiente nivel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrownText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val progress = (currentPoints - level.pointsRequired).toFloat() /
                            (nextLevel - level.pointsRequired).toFloat()

                    LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PrimaryOrange,
                        trackColor = NeutralGray.copy(alpha = 0.3f)
                    )

                    Text(
                        text = "${nextLevel - currentPoints} puntos para el siguiente nivel",
                        fontSize = 12.sp,
                        color = BrownText.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Beneficios
            Text(
                text = "Beneficios actuales:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = BrownText,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            level.benefits.forEach { benefit ->
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = PrimaryOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = benefit,
                        fontSize = 13.sp,
                        color = BrownText.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatsGrid(stats: UserStats) {
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
                text = "Estadísticas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Primera fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    title = "Compras",
                    value = "${stats.totalPurchases}",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "Canjes",
                    value = "${stats.totalSwaps}",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "Pts Ganados",
                    value = "${stats.totalPointsEarned}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Segunda fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    title = "Pts Gastados",
                    value = "${stats.totalPointsSpent}",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "Promedio",
                    value = "S/ ${decimalFormat.format(stats.averageOrderValue)}",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "Racha",
                    value = "${stats.recycleStreak} días",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tercera fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    title = "Este Mes",
                    value = "${stats.monthlyPoints} pts",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "Esta Semana",
                    value = "${stats.weeklyPoints} pts",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    title = "",
                    value = "",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryOrange,
            textAlign = TextAlign.Center
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = BrownText.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RecentActivitiesSection(activities: List<RecentActivity>) {
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
                text = "Actividad Reciente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            activities.forEach { activity ->
                ActivityItem(activity = activity)
                if (activity != activities.last()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = NeutralGray.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icono de actividad
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = getActivityColor(activity.type),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getActivityIcon(activity.type),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Información de la actividad
            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = activity.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = BrownText
                )
                Text(
                    text = activity.date,
                    fontSize = 12.sp,
                    color = BrownText.copy(alpha = 0.6f)
                )
            }
        }

        // Puntos
        Text(
            text = if (activity.points > 0) "+${activity.points}" else "${activity.points}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (activity.points > 0) PrimaryOrange else ErrorRed
        )
    }
}

@Composable
fun getActivityIcon(type: ActivityType): ImageVector {
    return when (type) {
        ActivityType.PURCHASE -> Icons.Default.ShoppingCart
        ActivityType.SWAP -> Icons.Default.Remove
        ActivityType.BONUS -> Icons.Default.Add
    }
}

@Composable
fun getActivityColor(type: ActivityType): Color {
    return when (type) {
        ActivityType.PURCHASE -> PrimaryOrange
        ActivityType.SWAP -> PurpleAccent
        ActivityType.BONUS -> YellowSoft
    }
}