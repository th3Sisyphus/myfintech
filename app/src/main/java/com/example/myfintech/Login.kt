package com.example.myfintech

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun Login(modifier: Modifier = Modifier,onLoginClicked:()->Unit,onCreatedAccountClicked:()->Unit ) {
    Surface(
        color = Color(0xFFF9FAFB),
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

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
                modifier = Modifier.fillMaxSize().padding(top = 100.dp).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
            ) {
                // Logo
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

                        // Inputs
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            // Email
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = { Text("Email address") },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null)
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.width(260.dp)
                            )

                            // Password
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = { Text("Password") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null)
                                },
                                trailingIcon = {
                                    Icon(Icons.Default.Visibility, contentDescription = null)
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.width(260.dp)
                            )

                            // Sign In Button
                            Box(
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF2B7FFF),
                                                Color(0xFFAD46FF)
                                            )
                                        )
                                    ).clickable { onLoginClicked() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Sign In",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.width(260.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Forgot password?",
                                color = Color(0xFF2B7FFF),
                                fontSize = 14.sp
                            )
                            Text(
                                "Create account",
                                color = Color(0xFFAD46FF),
                                fontSize = 14.sp,
                                modifier = Modifier.clickable {onCreatedAccountClicked()}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 385, heightDp = 852)
@Composable
private fun LoginPreview() {
    Login(onLoginClicked = {}, onCreatedAccountClicked = {})
}
