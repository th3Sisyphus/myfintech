package com.example.myfintech.domain.repository

import com.example.myfintech.common.Resource

interface AuthRepository {
    // Mengembalikan Resource<Boolean> untuk memberitahu sukses/gagal beserta pesannya
    suspend fun login(email: String, password: String): Resource<Boolean>

    suspend fun register(name: String, email: String, password: String): Resource<Boolean>

    suspend fun signInWithGoogle(): Resource<Boolean>

    suspend fun logout()

    // Cek apakah user sedang login (biasanya cek token di SharedPreferences/DataStore)
    fun isAuthenticated(): Boolean

    suspend fun updateProfile(name: String, email: String): Resource<Boolean>
    suspend fun changePassword(currentPassword: String, newPassword: String): Resource<Boolean>
    suspend fun forgotPassword(email: String): Resource<Boolean>
}