package com.example.myfintech.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // State untuk data user (Nama, Email) - bisa diambil dari UserUseCase nanti
    private val _userName = MutableStateFlow("Rainie Fanita")
    val userName = _userName.asStateFlow()

    private val _email = MutableStateFlow("rainie.fanita@ti.ukdw.ac.id")
    val email = _email.asStateFlow()

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLogoutSuccess()
        }
    }
}