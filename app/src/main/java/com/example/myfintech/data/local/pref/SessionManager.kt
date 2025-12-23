package com.example.myfintech.data.local.pref

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
val Context.dataStore by preferencesDataStore("user_session")

class SessionManager(private val context: Context) {
    companion object {
        val EMAIL = stringPreferencesKey("email")
        val FULLNAME = stringPreferencesKey("fullname")
    }

    // Save session
    suspend fun saveUserSession(email: String, fullname: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL] = email
            prefs[FULLNAME] = fullname
        }
    }

    // Read session
    private val userEmail: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[EMAIL] }

    private val fullname: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[FULLNAME] }

    // Delete session (logout)
    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    fun getFullname(): Flow<String?> {
        return fullname
    }

    // Mengembalikan Flow email
    fun getEmail(): Flow<String?> {
        return userEmail
    }
}

