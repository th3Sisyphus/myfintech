package com.example.myfintech

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfintech.data.auth.BiometricAuthenticator
import com.example.myfintech.data.auth.GoogleAuthClient
import com.example.myfintech.data.local.database.DatabaseProvider
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.ui.auth.Login
import com.example.myfintech.ui.auth.Register
import com.example.myfintech.ui.components.BottomNav
import com.example.myfintech.ui.components.LoadingScreen
import com.example.myfintech.ui.home.Analytic
import com.example.myfintech.ui.home.Home
import com.example.myfintech.ui.profile.PersonalInformationScreen
import com.example.myfintech.ui.profile.Profile
import com.example.myfintech.ui.theme.MyFintechTheme
import com.example.myfintech.ui.transaction.list.Transactions
import com.example.myfintech.viewmodel.AccountViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseProvider.getDatabase(applicationContext)
        val sessionManager = SessionManager(applicationContext)
        val googleAuthClient = GoogleAuthClient(this)
        val biometricAuthenticator = BiometricAuthenticator(this)

        val accountViewModel = AccountViewModel(
            dao = database.accountDao(),
            session = sessionManager,
            googleAuth = googleAuthClient,
            biometricAuth = biometricAuthenticator
        )

        setContent {
            MyFintechTheme {
                MainApp(this, accountViewModel)
            }
        }
    }
}

// A state machine to manage the app's authentication status safely.
enum class AuthState { UNKNOWN, AUTHENTICATED, NOT_AUTHENTICATED }

@Composable
fun MainApp(activity: FragmentActivity, accountViewModel: AccountViewModel) {
    val navController = rememberNavController()

    var authState by remember { mutableStateOf(AuthState.UNKNOWN) }

    // This effect runs once on startup to determine the auth state.
    LaunchedEffect(Unit) {
        val lastEmail = accountViewModel.session.getEmail().first()
        val hasSession = !lastEmail.isNullOrBlank()

        if (hasSession) {
            // If a session exists, check for biometrics.
            if (accountViewModel.isBiometricAvailable()) {
                accountViewModel.loginWithBiometric(activity) { success, _ ->
                    if (success) {
                        authState = AuthState.AUTHENTICATED
                    } else {
                        // On failure or cancel, force logout and set to not authenticated.
                        accountViewModel.logout {}
                        authState = AuthState.NOT_AUTHENTICATED
                    }
                }
            } else {
                // If no biometrics, but a session exists, the user is authenticated.
                authState = AuthState.AUTHENTICATED
            }
        } else {
            // If no session exists, the user is not authenticated.
            authState = AuthState.NOT_AUTHENTICATED
        }
    }

    when (authState) {
        AuthState.UNKNOWN -> {
            // Show a loading spinner while we determine the auth state.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        AuthState.AUTHENTICATED, AuthState.NOT_AUTHENTICATED -> {
            // Only build the UI once we know the definitive auth state.
            val startDestination = if (authState == AuthState.AUTHENTICATED) "loading" else "login"

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
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") {
                        Home(onProfileClicked = { navController.navigate("profile") })
                    }
                    composable("transactions") { Transactions() }
                    composable("analytics") { Analytic() }
                    composable("profile") {
                        Profile(
                            viewModel = accountViewModel,
                            onPersonalInfoClicked = { navController.navigate("personal_info") },
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
                    composable("personal_info") {
                        PersonalInformationScreen(
                            viewModel = accountViewModel,
                            onBackClicked = { navController.popBackStack() })
                    }
                    composable("login") {
                        Login(
                            activity = activity,
                            viewModel = accountViewModel,
                            onLoginClicked = { navController.navigate("loading") },
                            onCreatedAccountClicked = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        Register(
                            viewModel = accountViewModel,
                            onRegisterClicked = { navController.navigate("login") { popUpTo(0) { inclusive = true } } },
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
    }
}
