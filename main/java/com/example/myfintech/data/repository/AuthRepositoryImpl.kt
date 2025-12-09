package com.example.myfintech.data.repository

import android.content.SharedPreferences
import com.example.myfintech.common.Resource
import com.example.myfintech.data.remote.AuthApiService
import com.example.myfintech.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val sharedPreferences: SharedPreferences // Untuk simpan status login
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<Boolean> {
        return try {
            // Panggil API (Simulasi)
            val isSuccess = apiService.loginUser(email, password)

            if (isSuccess) {
                // Simpan token/flag login ke penyimpanan lokal
                sharedPreferences.edit().putBoolean("IS_LOGGED_IN", true).apply()
                Resource.Success(true)
            } else {
                Resource.Error("Invalid email or password")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun register(name: String, email: String, password: String): Resource<Boolean> {
        return try {
            val isSuccess = apiService.registerUser(name, email)
            if (isSuccess) {
                // Auto login setelah register (opsional)
                sharedPreferences.edit().putBoolean("IS_LOGGED_IN", true).apply()
                Resource.Success(true)
            } else {
                Resource.Error("Registration failed")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun signInWithGoogle(): Resource<Boolean> {
        return try {
            // TODO: Implementasi Google Credential Manager disini
            // Ini butuh library 'androidx.credentials:credentials' dan 'com.google.android.libraries.identity.googleid:googleid'

            kotlinx.coroutines.delay(1500) // Simulasi
            sharedPreferences.edit().putBoolean("IS_LOGGED_IN", true).apply()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Google Sign In Failed")
        }
    }

    override suspend fun logout() {
        // Hapus data sesi
        sharedPreferences.edit().clear().apply()
    }

    override fun isAuthenticated(): Boolean {
        // Cek apakah user sudah login sebelumnya (Persistence)
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }
}