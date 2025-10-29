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
fun Register(modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 385.dp)
                .requiredHeight(height = 852.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 385.dp)
                    .requiredHeight(height = 853.dp)
                    .background(color = Color(0xfff9fafb))
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 45.dp, y = 57.dp)
                        .requiredWidth(width = 278.dp)
                        .requiredHeight(height = 198.dp)
                        .clip(shape = RoundedCornerShape(30504000.dp))
                        .blur(radius = 48.dp)
                        .background(brush = Brush.linearGradient(
                            0f to Color(0xff2b7fff).copy(alpha = 0.2f),
                            0.5f to Color(0xffad46ff).copy(alpha = 0.2f),
                            1f to Color(0xff2b7fff).copy(alpha = 0.2f),
                            start = Offset(0f, 98.78f),
                            end = Offset(278.42f, 98.78f)))
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.Top),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp, y = 253.dp)
                        .requiredWidth(width = 385.dp)
                        .requiredHeight(height = 512.dp)
                        .padding(start = 24.dp, end = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(height = 340.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .requiredWidth(width = 345.dp)
                                .requiredHeight(height = 348.dp)
                                .clip(shape = RoundedCornerShape(32.dp))
                                .blur(radius = 16.dp)
                                .background(brush = Brush.linearGradient(
                                    0f to Color(0xff155dfc),
                                    0.5f to Color(0xff9810fa),
                                    1f to Color(0xff155dfc),
                                    start = Offset(172.5f, 0f),
                                    end = Offset(172.5f, 347.93f)))
                        )
                        Surface(
                            shape = RoundedCornerShape(30.dp),
                            color = Color.White,
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(x = 0.dp, y = 0.dp)
                                .clip(shape = RoundedCornerShape(30.dp))
                                .shadow(elevation = 50.dp, shape = RoundedCornerShape(30.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .requiredWidth(width = 337.dp)
                                    .requiredHeight(height = 471.dp)
                            ) {
                                // --- "Register" Title with Icon ---
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopCenter)
                                        .offset(y = 32.dp)
                                        .height(24.dp)
                                ) {
                                    // FIXED: Changed to an appropriate ImageVector
                                    Image(
                                        imageVector = Icons.Filled.AppRegistration,
                                        contentDescription = "Register Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Register",
                                        color = Color(0xff0a0a0a),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 1.5.em,
                                        style = TextStyle(fontSize = 16.sp)
                                    )
                                }
                                // --- Input Fields ---
                                Column(
                                    modifier = Modifier
                                        .align(alignment = Alignment.TopCenter)
                                        .offset(y = 80.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    // --- Email Field ---
                                    Box(modifier = Modifier.requiredWidth(273.dp).height(48.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(shape = RoundedCornerShape(8.dp))
                                                .background(color = Color(0xfff9fafb))
                                                .border(border = BorderStroke(1.dp, Color(0xffe5e7eb)), shape = RoundedCornerShape(8.dp))
                                                .padding(horizontal = 12.dp)
                                        ) {
                                            // FIXED: Using Email Icon
                                            Image(
                                                imageVector = Icons.Filled.Email,
                                                contentDescription = "Email Icon",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Email address",
                                                color = Color(0xff717182),
                                                style = TextStyle(fontSize = 14.sp)
                                            )
                                        }
                                    }
                                    // --- Password Field ---
                                    Box(modifier = Modifier.requiredWidth(273.dp).height(48.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(shape = RoundedCornerShape(8.dp))
                                                .background(color = Color(0xfff9fafb))
                                                .border(border = BorderStroke(1.dp, Color(0xffe5e7eb)), shape = RoundedCornerShape(8.dp))
                                                .padding(horizontal = 12.dp)
                                        ) {
                                            // FIXED: Using Lock Icon
                                            Image(
                                                imageVector = Icons.Filled.Lock,
                                                contentDescription = "Password Icon",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Password",
                                                color = Color(0xff717182),
                                                style = TextStyle(fontSize = 14.sp),
                                                modifier = Modifier.weight(1f)
                                            )
                                            // FIXED: Using Visibility Icon
                                            Image(
                                                imageVector = Icons.Filled.Visibility,
                                                contentDescription = "Show Password",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    // --- Confirm Password Field ---
                                    Box(modifier = Modifier.requiredWidth(273.dp).height(48.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(shape = RoundedCornerShape(8.dp))
                                                .background(color = Color(0xfff9fafb))
                                                .border(border = BorderStroke(1.dp, Color(0xffe5e7eb)), shape = RoundedCornerShape(8.dp))
                                                .padding(horizontal = 12.dp)
                                        ) {
                                            // FIXED: Using Lock Icon
                                            Image(
                                                imageVector = Icons.Filled.Lock,
                                                contentDescription = "Confirm Password Icon",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Confirm password",
                                                color = Color(0xff717182),
                                                style = TextStyle(fontSize = 14.sp),
                                                modifier = Modifier.weight(1f)
                                            )
                                            // FIXED: Using Visibility Icon
                                            Image(
                                                imageVector = Icons.Filled.Visibility,
                                                contentDescription = "Show Password",
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    // --- Sign Up Button ---
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .requiredWidth(width = 273.dp)
                                            .height(height = 48.dp)
                                            .clip(shape = RoundedCornerShape(8.dp))
                                            .background(brush = Brush.linearGradient(
                                                0f to Color(0xff155dfc),
                                                1f to Color(0xff9810fa),
                                                start = Offset(0f, 0f),
                                                end = Offset(273f, 48f)))
                                    ) {
                                        Text(
                                            text = "Sign up",
                                            color = Color.White,
                                            lineHeight = 1.43.em,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                    }
                                }
                                // --- Login Link ---
                                Row(
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomCenter)
                                        .padding(bottom = 80.dp) // Adjust as needed
                                ) {
                                    Text(
                                        text = "Already have an account? ",
                                        color = Color(0xff155dfc),
                                        style = TextStyle(fontSize = 16.sp)
                                    )
                                    Text(
                                        text = "Login here",
                                        color = Color(0xff9810fa),
                                        style = TextStyle(fontSize = 16.sp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // --- Top "MyFintech" Header ---
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(y = 112.dp),
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
                            .background(brush = Brush.linearGradient(
                                0f to Color(0xff155dfc),
                                1f to Color(0xff9810fa)))
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
                    text = "Welcome to Fintech! Please sign up here",
                    color = Color(0xff4a5565),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
}

@Preview(widthDp = 385, heightDp = 852)
@Composable
private fun RegisterPreview() {
    Register(Modifier)
}
