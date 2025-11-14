package com.example.myfintech

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
//            .shadow(elevation = 8.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            isSelected = currentRoute == "home",
            onClick = { onNavigate("home") }
        )
        BottomNavItem(
            label = "Transactions",
            selectedIcon = Icons.Filled.Paid,
            unselectedIcon = Icons.Outlined.Paid,
            isSelected = currentRoute == "transactions",
            onClick = { onNavigate("transactions") }
        )
        BottomNavItem(
            label = "Analytics",
            selectedIcon = Icons.Filled.Analytics,
            unselectedIcon = Icons.Filled.Analytics,
            isSelected = currentRoute == "analytics",
            onClick = { onNavigate("analytics") }
        )
        BottomNavItem(
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            isSelected = currentRoute == "profile",
            onClick = { onNavigate("profile") }
        )
    }
}

@Composable
private fun RowScope.BottomNavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = Color(0xff155dfc)
    val inactiveColor = Color(0xff6a7282)

    val color = if (isSelected) activeColor else inactiveColor
    val icon = if (isSelected) selectedIcon else unselectedIcon

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
    ) {
        Image(
            imageVector = icon,
            contentDescription = label,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavPreview() {
    BottomNav(
        currentRoute = "home",
        onNavigate = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun BottomNavPreview_Transactions() {
    BottomNav(
        currentRoute = "transactions",
        onNavigate = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun BottomNavPreview_Analytics() {
    BottomNav(
        currentRoute = "analytics",
        onNavigate = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun BottomNavPreview_Profile() {
    BottomNav(
        currentRoute = "profile",
        onNavigate = {}
    )
}