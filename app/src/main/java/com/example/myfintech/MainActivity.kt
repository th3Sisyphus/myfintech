package com.example.myfintech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfintech.ui.auth.Login
import com.example.myfintech.ui.home.Home
import com.example.myfintech.ui.theme.MyFintechTheme
import com.example.myfintech.ui.components.BottomNav
import com.example.myfintech.ui.components.LoadingScreen
import com.example.myfintech.ui.home.Analytic
import com.example.myfintech.ui.transaction.list.Transactions
import com.example.myfintech.ui.auth.Register
import com.example.myfintech.ui.profile.Profile
import com.example.myfintech.data.auth.GoogleAuthClient
import com.example.myfintech.data.local.database.DatabaseProvider
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.viewmodel.AccountViewModel

import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseProvider.getDatabase(applicationContext)
        val sessionManager = SessionManager(applicationContext)
        val googleAuthClient = GoogleAuthClient(applicationContext)

        val accountViewModel = AccountViewModel(
            dao = database.accountDao(),
            session = sessionManager,
            googleAuth = googleAuthClient
        )

        accountViewModel.loadUserData()

        setContent {
            MyFintechTheme {
                MainApp(accountViewModel)
            }
        }
    }
}

@Composable
fun MainApp(accountViewModel: AccountViewModel) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val isMainScreen = currentRoute in listOf("home", "transactions", "analytics", "profile")
            if (isMainScreen) {
                BottomNav(
                    currentRoute = currentRoute ?: "home",
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
                Analytic()
            }
            composable("profile") {
                Profile(
                    onLogOutClicked = {
                        accountViewModel.logout {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
            composable("login") {
                Login(viewModel = accountViewModel,
                    onLoginClicked = { navController.navigate("loading") },
                    onCreatedAccountClicked = { navController.navigate("register") }
                )
            }
            composable("register") {
                Register(
                    viewModel = accountViewModel,
                    onRegisterClicked = { navController.navigate("login"){
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    } },
                    onLoginClicked = { navController.navigate("login") },
                    onGoogleSignUpSuccess = { navController.navigate("loading") }

                )
            }
            composable("loading") {
                LoadingScreen()
                LaunchedEffect(Unit) {
                    delay(3000)
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}