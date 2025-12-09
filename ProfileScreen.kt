package com.example.myfintech.presentation.main.profile

import android.widget.Toast
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogOutClicked: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Effect untuk menampilkan Toast pesan sukses/error
    LaunchedEffect(state.message) {
        state.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.messageShown()
        }
    }

    // --- DIALOGS ---
    if (state.isEditProfileOpen) {
        EditProfileDialog(
            currentName = state.name,
            currentEmail = state.email, // Email dikirim tapi read-only
            isLoading = state.isLoading,
            onDismiss = { viewModel.closeEditProfile() },
            onSave = { newName -> viewModel.saveProfile(newName) }
        )
    }

    if (state.isChangePasswordOpen) {
        ChangePasswordDialog(
            isLoading = state.isLoading,
            onDismiss = { viewModel.closeChangePassword() },
            onSave = { old, new -> viewModel.changePassword(old, new) }
        )
    }

    // --- MAIN UI ---
    Surface(modifier = modifier.fillMaxSize(), color = Color(0xfff9fafb)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { ProfileHeader(state.name, state.email) }
            item { AccountDetailsCard(state.email) }
            item {
                SettingsMenu(
                    onEditProfileClick = { viewModel.openEditProfile() },
                    onChangePasswordClick = { viewModel.openChangePassword() }
                )
            }
            item {
                LogOutButton(onLogOutClicked = { viewModel.logout(onLogOutClicked) })
            }
        }
    }
}

// --- COMPONENT BARU: Edit Profile Dialog ---
@Composable
fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Personal Info") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Nama (Bisa diedit)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true
                )
                // Email (Read-Only / Disabled)
                OutlinedTextField(
                    value = currentEmail,
                    onValueChange = {},
                    label = { Text("Email (Cannot be changed)") },
                    enabled = false, // DISABLED
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Gray,
                        disabledBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name) },
                enabled = !isLoading && name.isNotEmpty()
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                else Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// --- COMPONENT BARU: Change Password Dialog ---
@Composable
fun ChangePasswordDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    label = { Text("Old Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it; errorMsg = null },
                    label = { Text("Confirm New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    isError = errorMsg != null,
                    supportingText = { if (errorMsg != null) Text(errorMsg!!, color = MaterialTheme.colorScheme.error) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPass != confirmPass) {
                        errorMsg = "Passwords do not match!"
                    } else {
                        onSave(oldPass, newPass)
                    }
                },
                enabled = !isLoading && oldPass.isNotEmpty() && newPass.isNotEmpty()
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                else Text("Update Password")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// --- MODIFIKASI: Settings Menu menerima aksi klik ---
@Composable
private fun SettingsMenu(
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileMenuItem(
            icon = Icons.Filled.Person,
            title = "Personal Information",
            subtitle = "Manage your details",
            onClick = onEditProfileClick // Link ke ViewModel
        )
        ProfileMenuItem(
            icon = Icons.Filled.Security,
            title = "Security",
            subtitle = "Change password",
            onClick = onChangePasswordClick // Link ke ViewModel
        )
    }
}

// --- Helper Components (Header, dll) tetap sama, hanya ProfileMenuItem perlu parameter onClick ---
@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
            .clickable { onClick() } // Handle Klik Disini
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

// (Sisa komponen helper seperti ProfileHeader, AccountDetailsCard, LogOutButton
// bisa Anda copy dari file ProfileScreen.kt sebelumnya karena tidak berubah logika internalnya,
// hanya pastikan import-nya benar).
@Composable
private fun ProfileHeader(name: String, email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp).clip(CircleShape)
                .background(brush = Brush.linearGradient(0f to Color(0xff2b7fff), 1f to Color(0xff9810fa)))
        ) {
            Text(text = name.take(2).uppercase(), color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
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
        // Reuse row without click
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xfff3f4f6))) {
                Image(imageVector = Icons.Filled.Email, contentDescription = "Email", modifier = Modifier.size(20.dp))
            }
            Column {
                Text(text = "Email", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Text(text = email, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
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