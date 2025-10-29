package com.example.replyapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// Import necessary icon sets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset // Import for Brush
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Profile(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xfff9fafb) // Main background color
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content area
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 69.dp), // Space for the bottom navigation bar
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Profile Header ---
                item {
                    ProfileHeader()
                }

                // --- Account Details Card ---
                item {
                    AccountDetailsCard()
                }

                // --- Menu Items ---
                item {
                    SettingsMenu()
                }

                // --- Log Out Button ---
                item {
                    LogOutButton()
                }
            }

            // --- Bottom Navigation Bar ---
            BottomNavigationBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

// --- Reusable Components (kept within the same file) ---

@Composable
fun ProfileHeader() {
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
                text = "RF",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Rainie Fanita",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "rainie.fanita@ti.ukdw.ac.id",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AccountDetailsCard() {
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
        // FIXED: Using real icons
        ProfileInfoRow(icon = Icons.Filled.Email, title = "Email", subtitle = "rainie.fanita@ti.ukdw.ac.id")
        ProfileInfoRow(icon = Icons.Filled.Phone, title = "Phone", subtitle = "+62 812-2592-8281")
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, title: String, subtitle: String) {
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
fun SettingsMenu() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // FIXED: Using real icons
        ProfileMenuItem(icon = Icons.Filled.Person, title = "Personal Information", subtitle = "Manage your details")
        ProfileMenuItem(icon = Icons.Filled.Notifications, title = "Notifications", subtitle = "Configure alerts")
        ProfileMenuItem(icon = Icons.Filled.Security, title = "Security", subtitle = "Change password")
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
            .clickable { }
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
        // FIXED: Using a real icon
        Image(imageVector = Icons.Filled.ChevronRight, contentDescription = "Go")
    }
}

@Composable
fun LogOutButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xffffc9c9)), RoundedCornerShape(8.dp))
            .clickable { /* TODO: Handle logout */ }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // FIXED: Using a real icon
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

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .shadow(elevation = 6.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // IMPROVED: Provide both filled and outlined icons
        BottomNavItem(
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        )
        BottomNavItem(
            label = "Transactions",
            selectedIcon = Icons.Filled.CompareArrows,
            unselectedIcon = Icons.Outlined.CompareArrows,
        )
        BottomNavItem(
            label = "Analytics",
            selectedIcon = Icons.Filled.Analytics,
            unselectedIcon = Icons.Outlined.Analytics,
        )
        BottomNavItem(
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            isSelected = true
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean = false
) {
    val color = if (isSelected) Color(0xff155dfc) else Color(0xff6a7282)
    // IMPROVED: Choose the icon based on the selection state
    val icon = if (isSelected) selectedIcon else unselectedIcon

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { /* TODO: Handle navigation */ }
    ) {
        Image(imageVector = icon, contentDescription = label, colorFilter = ColorFilter.tint(color))
        Text(text = label, color = color, style = MaterialTheme.typography.bodySmall, fontSize = 12.sp)
    }
}


@Preview(showBackground = true, widthDp = 385, heightDp = 852)
@Composable
private fun ProfilePreview() {
    Profile(Modifier)
}
