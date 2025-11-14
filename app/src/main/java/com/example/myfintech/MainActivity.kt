package com.example.myfintech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfintech.ui.theme.ReplyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReplyAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val isMainScreen = currentRoute in listOf("home", "transactions", "analytics", "profile")
            if (isMainScreen) {
                BottomNav(
                    currentRoute = currentRoute!!,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Home(
                    onProfileClicked = {
                        navController.navigate("profile")
                    }
                )
            }
            composable("transactions") {
                Transactions()
            }
            composable("analytics") {
                Analytics()
            }
            composable("profile") {
                Profile(
                    onLogOutClicked = { navController.navigate("login"){
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    } }
                )
            }
             composable("login") {
                 Login(
                     onLoginClicked = { navController.navigate("home"){
                         popUpTo(0) { inclusive = true }
                         launchSingleTop = true
                     } },
                     onCreatedAccountClicked = { navController.navigate("register") }
                 )
             }
             composable("register") {
                 Register(
                     onRegisterClicked = { navController.navigate("home"){
                         popUpTo(0) { inclusive = true }
                         launchSingleTop = true
                     } },
                     onLoginClicked = { navController.navigate("login") }
                 )
             }
        }
    }
}