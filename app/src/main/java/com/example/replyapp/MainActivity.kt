package com.example.replyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.replyapp.frontend.EmailDetail
import com.example.replyapp.frontend.ReplyListAndDetailContent
import com.example.replyapp.frontend.ReplyListOnlyContent
import com.example.replyapp.frontend.ReplyViewModel
import com.example.replyapp.ui.theme.ReplyAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReplyAppTheme {
                val windowSize = calculateWindowSizeClass(this).widthSizeClass
                ReplyApp(windowSize = windowSize)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    replyViewModel: ReplyViewModel = viewModel()
) {
    val replyUiState = replyViewModel.uiState.collectAsState().value
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Text("Mailboxes", modifier = Modifier.padding(16.dp))
                        Divider()
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Inbox") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "inbox",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(route = "inbox") {
                            ReplyListOnlyContent(
                                replyUiState = replyUiState,
                                onEmailClicked = { selectedEmail ->
                                    replyViewModel.updateCurrentEmail(selectedEmail)
                                    // *** THIS IS THE CRUCIAL LINE THAT WAS MISSING ***
                                    navController.navigate("detail")
                                }
                            )
                        }
                        composable(route = "detail") {
                            replyUiState.currentEmail?.let {
                                EmailDetail(email = it)
                            }
                        }
                    }
                }
            }
        }
        else -> {
            ReplyListAndDetailContent(
                replyUiState = replyUiState,
                onEmailClicked = { selectedEmail ->
                    replyViewModel.updateCurrentEmail(selectedEmail)
                }
            )
        }
    }
}