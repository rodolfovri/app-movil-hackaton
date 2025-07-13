package com.rodolfo.hackaton.feature.login.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodolfo.hackaton.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    val loginUiState by viewModel.loginUiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginUiState) {
        if (loginUiState is LoginViewModel.LoginUiState.Success) {
            onLoginSuccess()
            Log.d("LoginScreen", "Login successful: ${(loginUiState as LoginViewModel.LoginUiState.Success).message}")
        }

        if (loginUiState is LoginViewModel.LoginUiState.Error) {
            Log.d("LoginScreen", "Error: ${(loginUiState as LoginViewModel.LoginUiState.Error).message}")
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CreamBackground,
                        OrangeLight.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Bienvenido",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BrownText,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Inicia sesión para continuar",
                fontSize = 16.sp,
                color = BrownText.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Card del formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = PrimaryOrange
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryOrange,
                            focusedLabelColor = PrimaryOrange,
                            cursorColor = PrimaryOrange,
                            focusedTextColor = BrownText.copy(alpha = 0.8f),
                            unfocusedTextColor = BrownText.copy(alpha = 0.6f),
                            selectionColors = TextSelectionColors(
                                handleColor = PrimaryOrange,
                                backgroundColor = PrimaryOrange.copy(alpha = 0.4f)
                            )
                        )
                    )

                    // Campo Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Contraseña",
                                tint = PrimaryOrange
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña",
                                    tint = NeutralGray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryOrange,
                            focusedLabelColor = PrimaryOrange,
                            cursorColor = PrimaryOrange,
                            focusedTextColor = BrownText.copy(alpha = 0.8f),
                            unfocusedTextColor = BrownText.copy(alpha = 0.6f),
                            selectionColors = TextSelectionColors(
                                handleColor = PrimaryOrange,
                                backgroundColor = PrimaryOrange.copy(alpha = 0.4f)
                            )
                        )
                    )

                    // Botón de Login
                    Button(
                        onClick = {
                            viewModel.login(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryOrange
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = loginUiState !is LoginViewModel.LoginUiState.Loading
                    ) {
                        if (loginUiState is LoginViewModel.LoginUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
//
//                    // Texto de "¿Olvidaste tu contraseña?"
//                    TextButton(
//                        onClick = { /* Acción para recuperar contraseña */ },
//                        modifier = Modifier.padding(top = 16.dp)
//                    ) {
//                        Text(
//                            text = "¿Olvidaste tu contraseña?",
//                            color = PurpleAccent,
//                            fontSize = 14.sp
//                        )
//                    }
                }
            }

            // Texto de registro
//            Row(
//                modifier = Modifier.padding(top = 32.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "¿No tienes cuenta? ",
//                    color = BrownText.copy(alpha = 0.7f),
//                    fontSize = 14.sp
//                )
//                TextButton(
//                    onClick = { /* Navegar a registro */ },
//                    contentPadding = PaddingValues(0.dp)
//                ) {
//                    Text(
//                        text = "Regístrate",
//                        color = PrimaryOrange,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
        }
    }
}