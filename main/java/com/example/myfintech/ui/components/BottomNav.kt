package com.example.myfintech.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myfintech.presentation.main.analytic.AnalyticScreen
import com.example.myfintech.presentation.main.home.Home
import com.example.myfintech.presentation.main.profile.ProfileScreen
import com.example.myfintech.presentation.main.transaction.TransactionScreen


// Rute Internal Bottom Nav
sealed class NavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavItem("home", Icons.Default.Home, "Home")
    object Transactions : NavItem("transactions", Icons.Default.Receipt, "Transaksi")
    object Analytics : NavItem("analytics", Icons.Default.Analytics, "Analitik")
    object Profile : NavItem("profile", Icons.Default.Person, "Profil")
}

val items = listOf(NavItem.Home, NavItem.Transactions, NavItem.Analytics, NavItem.Profile)

@Composable
fun BottomNav(navController: NavHostController) {
    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Semua Composable di sini akan berada di dalam scope NavHost
            composable(NavItem.Home.route) {
                Home(
                    onProfileClicked = {
                        navController.navigate(NavItem.Profile.route)
                    }
                )
            }
            composable(NavItem.Transactions.route) {
                TransactionScreen() // Akan dibuat di bagian 5
            }
            composable(NavItem.Analytics.route) {
                AnalyticScreen() // Akan dibuat di bagian 4
            }
            composable(NavItem.Profile.route) {
                ProfileScreen(
                    onLogOutClicked = {
                        // Kembali ke Auth Root setelah logout
                        // Harus pop semua state dan kembali ke Root utama di MainActivity
                        navController.navigate("auth_root") {
                            popUpTo("main_root") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Hindari penumpukan destinasi saat berpindah tab
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}