package com.example.myfintech.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Status untuk menentukan layar awal (loading, loggedIn, notLoggedIn)
    private val _initialLoadState = MutableStateFlow<InitialState>(InitialState.Loading)
    val initialLoadState = _initialLoadState.asStateFlow()

    init {
        checkInitialAuthentication()
    }

    private fun checkInitialAuthentication() {
        viewModelScope.launch {
            // Memberi waktu sebentar agar LoadingScreen sempat terlihat
            kotlinx.coroutines.delay(1000)

            val isAuthenticated = authRepository.isAuthenticated()
            if (isAuthenticated) {
                _initialLoadState.value = InitialState.Success(true)
            } else {
                _initialLoadState.value = InitialState.Success(false)
            }
        }
    }
}

sealed class InitialState {
    object Loading : InitialState()
    data class Success(val isAuthenticated: Boolean) : InitialState()
}