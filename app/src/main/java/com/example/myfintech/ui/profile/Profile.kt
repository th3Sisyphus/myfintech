package com.example.myfintech.ui.profile

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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfintech.viewmodel.AccountViewModel

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel, // Inject ViewModel
    onLogOutClicked: () -> Unit = {},
    onPersonalInfoClicked: () -> Unit // Callback navigasi
) {
    // State untuk Dialog Password
    var showSecurityDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xfff9fafb)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { ProfileHeader(viewModel) }
                item { AccountDetailsCard(viewModel) }
                item {
                    SettingsMenu(
                        onPersonalInfoClick = onPersonalInfoClicked,
                        onSecurityClick = { showSecurityDialog = true }
                    )
                }
                item { LogOutButton(viewModel, onLogOutClicked) }
            }
        }

        // Tampilkan Dialog jika state true
        if (showSecurityDialog) {
            ChangePasswordDialog(
                viewModel = viewModel,
                onDismiss = { showSecurityDialog = false }
            )
        }
    }
}

@Composable
private fun ProfileHeader(viewModel: AccountViewModel) {
    // Ambil data langsung dari ViewModel (Reactive)
    val email by viewModel.currentEmail.collectAsState()
    val username by viewModel.currentName.collectAsState()
    val initial = if (username.isNotEmpty()) username.first().uppercase() else "U"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(0xff2b7fff),
                        1f to Color(0xff9810fa)
                    )
                )
        ) {
            Text(
                text = initial,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = email,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AccountDetailsCard(viewModel: AccountViewModel) {
    val email by viewModel.currentEmail.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Account Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        ProfileInfoRow(icon = Icons.Filled.Email, title = "Email", subtitle = email)
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, title: String, subtitle: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xfff3f4f6))
        ) {
            Image(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp))
        }
        Column {
            Text(text = title, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            Text(text = subtitle, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SettingsMenu(
    onPersonalInfoClick: () -> Unit,
    onSecurityClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileMenuItem(
            icon = Icons.Filled.Person,
            title = "Personal Information",
            subtitle = "Manage your details",
            onClick = onPersonalInfoClick
        )
        ProfileMenuItem(
            icon = Icons.Filled.Security,
            title = "Security",
            subtitle = "Change password",
            onClick = onSecurityClick
        )
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
            .clickable { onClick() } // Wiring klik di sini
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xffdbeafe))
        ) {
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
private fun LogOutButton(viewModel: AccountViewModel, onLogOutClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xffffc9c9)), RoundedCornerShape(8.dp))
            .clickable {
                viewModel.logout { onLogOutClicked() }
            }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Filled.Logout,
            contentDescription = "Log Out",
            colorFilter = ColorFilter.tint(Color(0xffe7000b))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Log Out",
            color = Color(0xffe7000b),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// --- DIALOG COMPONENT ---
@Composable
fun ChangePasswordDialog(
    viewModel: AccountViewModel,
    onDismiss: () -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    var oldVis by remember { mutableStateOf(false) }
    var newVis by remember { mutableStateOf(false) }
    var confirmVis by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Change Password", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Old Password
                OutlinedTextField(
                    value = oldPass, onValueChange = { oldPass = it },
                    label = { Text("Current Password") },
                    visualTransformation = if (oldVis) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { oldVis = !oldVis }) { Icon(if (oldVis) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) } },
                    singleLine = true
                )
                // New Password
                OutlinedTextField(
                    value = newPass, onValueChange = { newPass = it },
                    label = { Text("New Password") },
                    visualTransformation = if (newVis) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { newVis = !newVis }) { Icon(if (newVis) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) } },
                    singleLine = true
                )
                // Confirm Password
                OutlinedTextField(
                    value = confirmPass, onValueChange = { confirmPass = it },
                    label = { Text("Confirm New Password") },
                    visualTransformation = if (confirmVis) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { IconButton(onClick = { confirmVis = !confirmVis }) { Icon(if (confirmVis) Icons.Default.Visibility else Icons.Default.VisibilityOff, null) } },
                    singleLine = true,
                    isError = newPass != confirmPass && confirmPass.isNotEmpty()
                )
                if (message.isNotEmpty()) {
                    Text(text = message, color = if (isSuccess) Color(0xFF4CAF50) else Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPass != confirmPass) { message = "Passwords do not match"; isSuccess = false }
                    else if (newPass.isEmpty()) { message = "Password cannot be empty"; isSuccess = false }
                    else {
                        viewModel.changePassword(oldPass, newPass) { success, msg ->
                            message = msg; isSuccess = success
                            if (success) { /* Bisa ditutup otomatis atau biarkan user melihat sukses */ }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B7FFF))
            ) { Text("Update") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = Color.Gray) }
        }
    )
}
