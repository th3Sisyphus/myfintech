package com.example.myfintech.presentation.ui.components

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
import androidx.navigation.compose.rememberNavController
import com.example.myfintech.presentation.main.analytic.AnalyticScreen
import com.example.myfintech.presentation.main.home.Home
import com.example.myfintech.presentation.main.profile.ProfileScreen
import com.example.myfintech.presentation.main.transaction.TransactionScreen

// Rute Internal Bottom Nav
sealed class NavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavItem("home_tab", Icons.Default.Home, "Home")
    object Transactions : NavItem("transactions_tab", Icons.Default.Receipt, "Transaksi")
    object Analytics : NavItem("analytics_tab", Icons.Default.Analytics, "Analitik")
    object Profile : NavItem("profile_tab", Icons.Default.Person, "Profil")
}

val items = listOf(NavItem.Home, NavItem.Transactions, NavItem.Analytics, NavItem.Profile)

@Composable
fun BottomNav(rootNavController: NavHostController) { // Parameter ini hanya untuk LOGOUT

    // PERBAIKAN: Buat controller BARU khusus untuk tab di dalam sini
    val tabNavController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomNavigationBar(tabNavController) } // Gunakan tabNavController
    ) { paddingValues ->
        NavHost(
            navController = tabNavController, // Gunakan tabNavController (JANGAN rootNavController)
            startDestination = NavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavItem.Home.route) {
                Home(
                    onProfileClicked = { tabNavController.navigate(NavItem.Profile.route) },
                )
            }
            composable(NavItem.Transactions.route) {
                TransactionScreen()
            }
            composable(NavItem.Analytics.route) {
                AnalyticScreen()
            }
            composable(NavItem.Profile.route) {
                ProfileScreen(
                    onLogOutClicked = {
                        // Saat Logout, barulah kita pakai rootNavController
                        // untuk 'membunuh' BottomNav dan kembali ke Login
                        rootNavController.navigate("auth_root") {
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