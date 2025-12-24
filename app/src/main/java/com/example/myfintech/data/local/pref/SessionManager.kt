package com.example.myfintech.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_session")

class SessionManager(private val context: Context) {
    companion object {
        val EMAIL = stringPreferencesKey("email")
        val FULLNAME = stringPreferencesKey("fullname")
    }

    suspend fun saveUserSession(email: String, fullname: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL] = email
            prefs[FULLNAME] = fullname
        }
    }

    private val userFlow: Flow<Preferences> = context.dataStore.data
        .catch { 
            // On any exception during datastore read, treat as a logout.
            // This makes the app resilient to file corruption.
            emit(emptyPreferences())
        }

    fun getEmail(): Flow<String?> {
        return userFlow.map { prefs -> prefs[EMAIL] }
    }

    fun getFullname(): Flow<String?> {
        return userFlow.map { prefs -> prefs[FULLNAME] }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
