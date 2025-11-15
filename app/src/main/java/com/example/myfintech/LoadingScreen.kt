package com.example.myfintech

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfintech.ui.theme.PurpleDark
import com.example.myfintech.ui.theme.PurpleLight // Pastikan ini diimpor atau sesuaikan dengan warna Anda

@Composable
fun LoadingScreen() {
    // State untuk animasi progress bar
    val progress by animateFloatAsState(
        targetValue = 1f, // Animasi dari 0 ke 1
        animationSpec = infiniteRepeatable( // Ulangi terus-menerus
            animation = tween(durationMillis = 1500, easing = LinearEasing), // Durasi 1.5 detik per siklus
            repeatMode = RepeatMode.Restart // Mulai ulang dari awal
        ), label = "LoadingProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)), // Warna latar belakang seperti aplikasi Anda
        contentAlignment = Alignment.Center
    ) {
        // Efek blur di tengah seperti di Login/Register
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo dan Nama Aplikasi
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
                                listOf(PurpleDark, PurpleLight) // Menggunakan warna tema Anda
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Anda bisa menambahkan ikon di sini jika ada (seperti Icons.Default.AccountBalance)
                    Text(
                        text = "$", // Placeholder, ganti dengan ikon jika ada
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "MyFintech",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5E17EB) // Warna teks MyFintech Anda
                    )
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Welcome back, Rainie Fanita !",
                color = Color(0xFF6B7280),
                fontSize = 16.sp
            )

            Spacer(Modifier.height(60.dp)) // Spacer untuk jarak ke progress bar

            Text(
                text = "Loading In Progress ...",
                color = Color(0xFF6B7280),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(10.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = progress, // Menggunakan nilai animasi
                modifier = Modifier
                    .width(200.dp) // Lebar progress bar
                    .height(8.dp) // Tinggi progress bar
                    .clip(RoundedCornerShape(4.dp)), // Rounded corners
                color = PurpleLight, // Warna utama progress bar
                trackColor = PurpleLight.copy(alpha = 0.3f) // Warna background track
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 385, heightDp = 852)
@Composable
fun LoadingScreenPreview() {
    MaterialTheme {
        LoadingScreen()
    }
}