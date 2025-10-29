package com.example.replyapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun Login(modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xfff9fafb))
        ) {
            // Blurred background shapes
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-80).dp)
                    .requiredWidth(width = 278.dp)
                    .requiredHeight(height = 198.dp)
                    .clip(shape = RoundedCornerShape(30504000.dp))
                    .blur(radius = 48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            0f to Color(0xff2b7fff).copy(alpha = 0.2f),
                            0.5f to Color(0xffad46ff).copy(alpha = 0.2f),
                            1f to Color(0xff2b7fff).copy(alpha = 0.2f),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            )

            // Main content column
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                // --- Top "MyFintech" Header ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(shape = RoundedCornerShape(14.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        0f to Color(0xff155dfc),
                                        1f to Color(0xff9810fa)
                                    )
                                )
                                .shadow(elevation = 6.dp, shape = RoundedCornerShape(14.dp))
                        ) {
                            // FIXED: Using a relevant icon
                            Image(
                                imageVector = Icons.Filled.AccountBalance,
                                contentDescription = "App Icon",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            text = "MyFintech",
                            textAlign = TextAlign.Center,
                            lineHeight = 1.em,
                            style = TextStyle(fontSize = 40.sp)
                        )
                    }
                    Text(
                        text = "Welcome back! Please login to continue",
                        color = Color(0xff4a5565),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                // --- Login Form Card ---
                Box {
                    // Blurred shadow effect
                    Box(
                        modifier = Modifier
                            .width(345.dp)
                            .height(348.dp)
                            .clip(shape = RoundedCornerShape(32.dp))
                            .blur(radius = 16.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    0f to Color(0xff155dfc),
                                    0.5f to Color(0xff9810fa),
                                    1f to Color(0xff155dfc)
                                )
                            )
                    )
                    // Main form surface
                    Surface(
                        shape = RoundedCornerShape(30.dp),
                        color = Color.White,
                        modifier = Modifier
                            .width(337.dp)
                            .shadow(elevation = 50.dp, shape = RoundedCornerShape(30.dp))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 32.dp)
                        ) {
                            // "Login" Title
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // FIXED: Using Login icon
                                Image(
                                    imageVector = Icons.Filled.Login,
                                    contentDescription = "Login Icon"
                                )
                                Text(
                                    text = "Login",
                                    color = Color(0xff0a0a0a),
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // --- Input Fields ---
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                // Email Field
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .width(273.dp)
                                        .height(48.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .background(color = Color(0xfff9fafb))
                                        .border(
                                            border = BorderStroke(1.dp, Color(0xffe5e7eb)),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp)
                                ) {
                                    Image(
                                        imageVector = Icons.Filled.Email,
                                        contentDescription = "Email Icon"
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Email address", color = Color(0xff717182))
                                }

                                // Password Field
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .width(273.dp)
                                        .height(48.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .background(color = Color(0xfff9fafb))
                                        .border(
                                            border = BorderStroke(1.dp, Color(0xffe5e7eb)),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp)
                                ) {
                                    Image(
                                        imageVector = Icons.Filled.Lock,
                                        contentDescription = "Password Icon"
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Password", color = Color(0xff717182), modifier = Modifier.weight(1f))
                                    Image(
                                        imageVector = Icons.Filled.Visibility,
                                        contentDescription = "Show Password"
                                    )
                                }

                                // Sign In Button
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .width(273.dp)
                                        .height(48.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .background(
                                            brush = Brush.linearGradient(
                                                0f to Color(0xff155dfc),
                                                1f to Color(0xff9810fa)
                                            )
                                        )
                                ) {
                                    Text(
                                        text = "Sign In",
                                        color = Color.White,
                                        style = TextStyle(fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // --- Footer Links ---
                            Row(
                                modifier = Modifier.width(273.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Forgot password?",
                                    color = Color(0xff155dfc),
                                    style = TextStyle(fontSize = 16.sp)
                                )
                                Text(
                                    text = "Create account",
                                    color = Color(0xff9810fa),
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }
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
    Login(Modifier)
}
