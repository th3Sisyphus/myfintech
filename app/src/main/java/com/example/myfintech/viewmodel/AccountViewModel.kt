package com.example.myfintech.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.data.auth.BiometricAuthenticator
import com.example.myfintech.data.auth.GoogleAuthClient
import com.example.myfintech.data.local.dao.AccountDao
import com.example.myfintech.data.local.entities.Account
import com.example.myfintech.data.local.pref.SessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AccountViewModel(
    private val dao: AccountDao,
    val session: SessionManager,
    private val googleAuth: GoogleAuthClient,
    private val biometricAuth: BiometricAuthenticator
) : ViewModel() {

    // This is unused, but we'll leave it in case you need it later.
    private val _emails = MutableStateFlow<List<String>>(emptyList())
    val emails: StateFlow<List<String>> = _emails.asStateFlow()

    val currentName: StateFlow<String> = session.getFullname()
        .map { it ?: "User" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "User"
        )

    val currentEmail: StateFlow<String> = session.getEmail()
        .map { it ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun initEmail() {
        viewModelScope.launch {
            _emails.value = dao.getAllEmails()
        }
    }

    fun isBiometricAvailable(): Boolean = biometricAuth.isBiometricAvailable()

    fun loginWithBiometric(activity: FragmentActivity, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val email = session.getEmail().first()
            if (email.isNullOrBlank()) {
                onResult(false, "No last logged in user found.")
                return@launch
            }

            biometricAuth.authenticate(activity, onSuccess = {
                // On success, we just notify the UI. The stateIn operator will handle data loading.
                onResult(true, null)
            }, onError = {
                onResult(false, it)
            })
        }
    }

    fun signInWithGoogle(context: Context, isLogin: Boolean, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val (success, errorMessage) = googleAuth.signIn(context)

            if (success) {
                val firebaseUser = googleAuth.getCurrentUser()
                firebaseUser?.let { user ->
                    val email = user.email ?: ""
                    val name = user.displayName ?: "Google User"

                    val existingAccount = dao.getAccountByEmail(email)

                    if (isLogin) {
                        if (existingAccount == null) {
                            googleAuth.signOut()
                            onResult(false, "Account not registered. Please sign up first.")
                            return@launch
                        }
                    } else { // Handle registration
                        if (existingAccount == null) {
                            val newAccount = Account(
                                fullname = name,
                                email = email,
                                password = "" // No password for Google sign-in
                            )
                            dao.register(newAccount)
                        }
                    }

                    session.saveUserSession(email, name)
                    onResult(true, null)
                } ?: onResult(false, "Firebase user is null")
            } else {
                onResult(false, errorMessage)
            }
        }
    }

    fun logout(onResult: () -> Unit) {
        viewModelScope.launch {
            googleAuth.signOut()
            session.clearSession()
            onResult()
        }
    }

    fun register(fullname: String, email: String, password: String, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            val existing = dao.getAccountByEmail(email)
            if (existing != null) {
                onResult(false, "Email already registered")
                return@launch
            }

            val account = Account(
                fullname = fullname,
                email = email,
                password = password  // you can hash later
            )

            dao.register(account)
            onResult(true, "Success")
        }
    }

    suspend fun login(email: String, password: String): Account? {
        val account = dao.getAccountByEmail(email)
        return if (account != null && account.password == password) {
            session.saveUserSession(
                email = account.email,
                fullname = account.fullname
            )
            account
        } else
            null
    }

    fun updateProfile(newName: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val email = currentEmail.value
            if (email.isBlank()) {
                onResult(false, "User not found")
                return@launch
            }
            val account = dao.getAccountByEmail(email)

            if (account != null) {
                val updatedAccount = account.copy(fullname = newName)
                dao.updateAccount(updatedAccount)

                session.saveUserSession(email, newName)
                onResult(true, "Profile updated successfully")
            } else {
                onResult(false, "User not found")
            }
        }
    }

    fun changePassword(oldPass: String, newPass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val email = currentEmail.value
            if (email.isBlank()) {
                onResult(false, "User not found")
                return@launch
            }
            val account = dao.getAccountByEmail(email)

            if (account != null) {
                if (account.password == oldPass) {
                    val updatedAccount = account.copy(password = newPass)
                    dao.updateAccount(updatedAccount)
                    onResult(true, "Password changed successfully")
                } else {
                    onResult(false, "Incorrect old password")
                }
            } else {
                onResult(false, "User error")
            }
        }
    }
}