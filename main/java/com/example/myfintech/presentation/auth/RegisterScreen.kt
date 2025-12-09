package com.example.myfintech.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onLoginClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onRegisterSuccess()
        }
    }

    if (state.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(AuthEvent.ErrorDismissed) },
            confirmButton = { TextButton(onClick = { viewModel.onEvent(AuthEvent.ErrorDismissed) }) { Text("OK") } },
            title = { Text("Error") },
            text = { Text(state.error ?: "") }
        )
    }

    RegisterContent(
        state = state,
        onEvent = viewModel::onEvent,
        onLoginClicked = onLoginClicked
    )
}

@Composable
fun RegisterContent(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onLoginClicked: () -> Unit
) {
    Surface(color = Color(0xFFF9FAFB), modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Blur
            Box(
                modifier = Modifier.align(Alignment.Center).size(300.dp).blur(180.dp)
                    .background(Brush.radialGradient(
                        colors = listOf(Color(0xFFAD46FF).copy(0.4f), Color(0xFF2B7FFF).copy(0.4f), Color.Transparent),
                        center = Offset(300f, 300f), radius = 400f
                    ))
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Logo (Sama seperti Login)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF2B7FFF), Color(0xFFAD46FF)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AccountBalance, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Text("MyFintech", style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5E17EB)))
                }

                Spacer(Modifier.height(8.dp))
                Text("Welcome! Please sign up below", color = Color(0xFF6B7280), fontSize = 16.sp)
                Spacer(Modifier.height(40.dp))

                Surface(
                    shape = RoundedCornerShape(28.dp), color = Color.White, shadowElevation = 12.dp,
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 32.dp)) {
                        Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(24.dp))

                        // --- INPUTS ---
                        val modifierField = Modifier.width(260.dp)

                        OutlinedTextField(
                            value = state.name,
                            onValueChange = { onEvent(AuthEvent.NameChanged(it)) },
                            label = { Text("Full name") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = modifierField
                        )
                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = state.email,
                            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                            label = { Text("Email") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = modifierField
                        )
                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = state.password,
                            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            trailingIcon = { Icon(Icons.Default.Visibility, null) }, // TODO: Implement toggle
                            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = modifierField
                        )
                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = state.repeatedPassword,
                            onValueChange = { onEvent(AuthEvent.RepeatedPasswordChanged(it)) },
                            label = { Text("Confirm Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = modifierField
                        )
                        Spacer(Modifier.height(24.dp))

                        // --- SIGN UP BUTTON ---
                        Box(
                            modifier = modifierField.height(48.dp).clip(RoundedCornerShape(12.dp))
                                .background(brush = if (state.isLoading) SolidColor(Color.Gray) else Brush.linearGradient(listOf(Color(0xFF2B7FFF), Color(0xFFAD46FF))))
                                .clickable(enabled = !state.isLoading) { onEvent(AuthEvent.RegisterClicked) },
                            contentAlignment = Alignment.Center
                        ) {
                            if(state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            else Text("Sign Up", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(Modifier.height(16.dp))

                        // --- GOOGLE SIGN IN BUTTON ---
                        OutlinedButton(
                            onClick = { onEvent(AuthEvent.GoogleSignInClicked) },
                            modifier = modifierField.height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !state.isLoading
                        ) {
                            // Icon Google (Placeholder Icon)
                            Icon(Icons.Default.Language, contentDescription = null, tint = Color.Gray)
                            Spacer(Modifier.width(8.dp))
                            Text("Sign up with Google", color = Color.Gray)
                        }

                        Spacer(Modifier.height(20.dp))

                        Row {
                            Text("Already have an account? ", color = Color(0xFF2B7FFF), fontSize = 14.sp)
                            Text("Login here", color = Color(0xFFAD46FF), fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onLoginClicked() })
                        }
                    }
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }
}