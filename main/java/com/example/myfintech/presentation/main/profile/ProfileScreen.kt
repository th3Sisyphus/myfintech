package com.example.myfintech.presentation.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogOutClicked: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val name by viewModel.userName.collectAsState()
    val email by viewModel.email.collectAsState()

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xfff9fafb)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileHeader(name, email)
            }
            item {
                AccountDetailsCard(email)
            }
            item {
                SettingsMenu()
            }
            item {
                // LOGOUT BUTTON MENGHUBUNGI VIEWMODEL
                LogOutButton(
                    onLogOutClicked = {
                        viewModel.logout(onLogoutSuccess = onLogOutClicked)
                    }
                )
            }
        }
    }
}

// ... (Komponen UI Bawahnya sama persis dengan file Profile.kt lama Anda) ...

@Composable
private fun ProfileHeader(name: String, email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp).clip(CircleShape)
                .background(brush = Brush.linearGradient(0f to Color(0xff2b7fff), 1f to Color(0xff9810fa)))
        ) {
            Text(text = "RF", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = email, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun AccountDetailsCard(email: String) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Account Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        ProfileInfoRow(icon = Icons.Filled.Email, title = "Email", subtitle = email)
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xfff3f4f6))) {
            Image(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp))
        }
        Column {
            Text(text = title, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            Text(text = subtitle, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SettingsMenu() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileMenuItem(icon = Icons.Filled.Person, title = "Personal Information", subtitle = "Manage your details")
        ProfileMenuItem(icon = Icons.Filled.Security, title = "Security", subtitle = "Change password")
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xffdbeafe))) {
            Image(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color(0xff155dfc)))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
        Image(imageVector = Icons.Filled.ChevronRight, contentDescription = "Go")
    }
}

@Composable
private fun LogOutButton(onLogOutClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.White)
            .border(BorderStroke(1.dp, Color(0xffffc9c9)), RoundedCornerShape(8.dp))
            .clickable { onLogOutClicked() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(imageVector = Icons.Filled.Logout, contentDescription = "Log Out", colorFilter = ColorFilter.tint(Color(0xffe7000b)))
        Spacer(Modifier.width(8.dp))
        Text(text = "Log Out", color = Color(0xffe7000b), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}