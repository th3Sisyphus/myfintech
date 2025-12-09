package com.example.myfintech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfintech.presentation.LoadingScreen
import com.example.myfintech.presentation.auth.LoginScreen
import com.example.myfintech.presentation.auth.RegisterScreen
import com.example.myfintech.presentation.main.MainViewModel
import com.example.myfintech.presentation.main.InitialState
import com.example.myfintech.ui.components.BottomNav
import com.example.myfintech.ui.theme.MyFintechTheme
import dagger.hilt.android.AndroidEntryPoint

// Ubah: Tambahkan @AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFintechTheme {
                val navController = rememberNavController()

                // Gunakan ViewModel untuk menentukan layar awal
                val viewModel: MainViewModel = hiltViewModel()
                val initialState by viewModel.initialLoadState.collectAsState()

                // Destinasi ROOT utama
                val authRoute = "auth_root"
                val mainRoute = "main_root"

                // Menentukan Rute Awal berdasarkan status loading
                val startDestination = when (initialState) {
                    is InitialState.Loading -> "splash_loading"
                    is InitialState.Success -> {
                        if ((initialState as InitialState.Success).isAuthenticated) mainRoute else authRoute
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    // Tampilkan Loading Screen saat status otentikasi sedang dicek
                    composable("splash_loading") {
                        LoadingScreen()
                    }

                    // Root Navigasi AUTH (Login & Register)
                    composable(authRoute) {
                        LoginScreen(
                            onLoginSuccess = {
                                // Ganti AuthRoot dengan MainRoot, hapus history navigasi auth
                                navController.navigate(mainRoute) {
                                    popUpTo(authRoute) { inclusive = true }
                                }
                            },
                            onCreatedAccountClicked = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = {
                                navController.navigate(mainRoute) {
                                    popUpTo(authRoute) { inclusive = true }
                                }
                            },
                            onLoginClicked = { navController.popBackStack() }
                        )
                    }

                    // Root Navigasi MAIN (Bottom Navigation)
                    composable(mainRoute) {
                        BottomNav(navController = navController)
                    }
                }
            }
        }
    }
}