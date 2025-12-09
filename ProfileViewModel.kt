package com.example.myfintech.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.myfintech.common.Resource
import kotlinx.coroutines.flow.update



data class ProfileUiState(
    val name: String = "Rainie Fanita",
    val email: String = "rainie.fanita@ti.ukdw.ac.id",
    val isLoading: Boolean = false,
    val message: String? = null, // Untuk Toast/Snackbar

    // Kontrol Dialog
    val isEditProfileOpen: Boolean = false,
    val isChangePasswordOpen: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    // --- EVENT HANDLERS ---

    // 1. Logika Update Profile
    fun openEditProfile() { _uiState.update { it.copy(isEditProfileOpen = true) } }
    fun closeEditProfile() { _uiState.update { it.copy(isEditProfileOpen = false) } }

    fun saveProfile(newName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.updateProfile(newName, _uiState.value.email)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isEditProfileOpen = false, name = newName, message = "Profil berhasil diupdate") }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, message = result.message) }
                }
                else -> {}
            }
        }
    }

    // 2. Logika Change Password
    fun openChangePassword() { _uiState.update { it.copy(isChangePasswordOpen = true) } }
    fun closeChangePassword() { _uiState.update { it.copy(isChangePasswordOpen = false) } }

    fun changePassword(oldPass: String, newPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.changePassword(oldPass, newPass)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isChangePasswordOpen = false, message = "Password berhasil diubah") }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, message = result.message) }
                }
                else -> {}
            }
        }
    }

    // 3. Reset Pesan (agar Toast tidak muncul terus)
    fun messageShown() { _uiState.update { it.copy(message = null) } }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLogoutSuccess()
        }
    }
}