package com.example.myfintech.ui.auth

import androidx.compose.foundation.BorderStroke
import com.example.myfintech.data.local.database.*
import com.example.myfintech.viewmodel.*
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.data.auth.GoogleAuthClient

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext



@Composable
fun Register(
    viewModel: AccountViewModel,
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onGoogleSignUpSuccess: () -> Unit
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
        val sessionManager = SessionManager(context)
        val __db = remember { DatabaseProvider.getDatabase(context) }
        val __accountDao = remember { __db.accountDao() }
        val __accountViewModel = remember { AccountViewModel(__accountDao,sessionManager,googleAuth = GoogleAuthClient(context)) }

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
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(top = 20.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF2B7FFF), Color(0xFFAD46FF))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "MyFintech",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5E17EB)
                        )
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Welcome back! Please login to continue",
                    color = Color(0xFF6B7280),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = null,
                                tint = Color(0xFF6B46C1)
                            )
                            Text(
                                text = "Login",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

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
                                        if (emailInput.isEmpty() || passwordInput.isEmpty() ) {
                                            errorMessage = "Password or Email cannot be empty"
                                        } else if (passwordInput != confirmPasswordInput) {
                                            errorMessage = "Confirm password is not the same"
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

                        Button(
                            onClick = {
                                viewModel.signInWithGoogle(context) { success, msg ->
                                    if (success) {
                                        onLoginClicked()
                                    } else {
                                        errorMessage = msg ?: "Sign in failed"
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(260.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Text(
                                text = "Sign In with Google",
                                color = Color.Black, // Text hitam di atas background putih
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.width(260.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "already have account?",
                                color = Color(0xFF2B7FFF),
                                fontSize = 14.sp
                            )
                            Text(
                                "Login here!",
                                color = Color(0xFFAD46FF),
                                fontSize = 14.sp,
                                modifier = Modifier.clickable {onLoginClicked()}
                            )
                        }
                    }
                }
            }
        }
    }
}
