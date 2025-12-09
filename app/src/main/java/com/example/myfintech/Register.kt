package com.example.myfintech

import com.example.myfintech.dao.*
import com.example.myfintech.db.*
import com.example.myfintech.entity.*
import com.example.myfintech.model.*


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext

@Composable
fun Register(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {}
) {
    Surface(
        color = Color(0xFFF9FAFB),
        modifier = modifier.fillMaxSize()
    ) {
        var fullnameInput by remember { mutableStateOf("") }
        var emailInput by remember { mutableStateOf("") }

        var passwordInput by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        var confirmPasswordInput by remember { mutableStateOf("") }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        // ERROR MESSAGE STATE
        var errorMessage by remember { mutableStateOf("") }

        // --- DATABASE & VIEWMODEL ---
        val context = LocalContext.current
        val __db = remember { DatabaseProvider.getDatabase(context) }
        val __accountDao = remember { __db.accountDao() }
        val __accountViewModel = remember { AccountViewModel(__accountDao) }

        Box(modifier = Modifier.fillMaxSize()) {

            // BACKGROUND GRADIENT LIGHT
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(300.dp)
                    .blur(180.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFAD46FF).copy(alpha = 0.4f),
                                Color(0xFF2B7FFF).copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            center = Offset(300f, 300f),
                            radius = 400f
                        )
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(Modifier.height(60.dp))

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White,
                    shadowElevation = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 32.dp)
                    ) {

                        Spacer(Modifier.height(24.dp))

                        // Input Fields
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            OutlinedTextField(
                                value = fullnameInput,
                                onValueChange = { fullnameInput = it },
                                label = { Text("Full name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.width(260.dp)
                            )

                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                label = { Text("Email address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.width(260.dp)
                            )

                            // PASSWORD FIELD
                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = {
                                    passwordInput = it
                                    errorMessage = ""   // clear error when typing
                                },
                                label = { Text("Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                trailingIcon = {
                                    val image = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff

                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(imageVector = image, contentDescription = null)
                                    }
                                },
                                visualTransformation = if (passwordVisible)
                                    VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.width(260.dp)
                            )

                            // CONFIRM PASSWORD
                            OutlinedTextField(
                                value = confirmPasswordInput,
                                onValueChange = {
                                    confirmPasswordInput = it
                                    errorMessage = ""  // clear error when typing
                                },
                                label = { Text("Confirm password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                trailingIcon = {
                                    val image = if (confirmPasswordVisible)
                                        Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff

                                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                        Icon(imageVector = image, contentDescription = null)
                                    }
                                },
                                visualTransformation = if (confirmPasswordVisible)
                                    VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.width(260.dp)
                            )

                            // ERROR MESSAGE
                            if (errorMessage.isNotEmpty()) {
                                Text(
                                    text = errorMessage,
                                    color = Color.Red,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            // SIGN UP BUTTON
                            Box(
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF2B7FFF), Color(0xFFAD46FF))
                                        )
                                    )
                                    .clickable {
                                        // VALIDATION LOGIC
                                        if (passwordInput != confirmPasswordInput) {
                                            errorMessage = "Confirm password is not the same"
                                        } else if (passwordInput.isEmpty()) {
                                            errorMessage = "Password cannot be empty"
                                        } else {
                                            errorMessage = ""

                                            __accountViewModel.register(
                                                fullnameInput,
                                                emailInput,
                                                passwordInput
                                            ) { success, msg ->
                                                if (!success) {
                                                    errorMessage = msg
                                                } else {
                                                    onRegisterClicked()
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Sign Up",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 385, heightDp = 852)
@Composable
private fun RegisterPreview() {
    Register(
        onRegisterClicked = {},
        onLoginClicked = {}
    )
}
