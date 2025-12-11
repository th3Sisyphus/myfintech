package com.example.myfintech.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myfintech.data.local.dao.*
import com.example.myfintech.data.local.database.*
import com.example.myfintech.data.local.pref.SessionManager

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.data.local.entities.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AccountViewModel(private val dao: AccountDao, private val session: SessionManager) : ViewModel() {

    private val _emailState = MutableStateFlow("")
    val emailState: StateFlow<String> = _emailState.asStateFlow()

    private val _emails = MutableStateFlow<List<String>>(emptyList())
    val emails: StateFlow<List<String>> = _emails.asStateFlow()

    private val _currentName = MutableStateFlow("User")
    val currentName: StateFlow<String> = _currentName.asStateFlow()

    private val _currentEmail = MutableStateFlow("")
    val currentEmail: StateFlow<String> = _currentEmail.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun initEmail() {
        viewModelScope.launch {
            _emails.value = dao.getAllEmails()
        }
    }

    fun signInWithGoogle(credential: AuthCredential, onResult: (Boolean, String) -> Unit ) {
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    val email = firebaseUser.email ?: ""
                    val name = firebaseUser.displayName ?: "Google User"

                    val existingAccount = dao.getAccountByEmail(email)
                    if (existingAccount == null) {
                        // Jika belum ada, buatkan akun lokal baru
                        val newAccount = Account(
                            fullname = name,
                            email = email,
                            password = "" // Password kosong karena login via Google
                        )
                        dao.register(newAccount)
                    }

                    session.saveUserSession(email, name)
                    loadUserData()
                    onResult(true, "Login Berhasil: $name")

                }else{
                    onResult(false, "Login Gagal: User tidak ditemukan")
                }
            } catch (e: Exception) {
                Log.e("AccountViewModel", "signInWithGoogle: ${e.message}", e)
                onResult(false, "Login Gagal: ${e.message}")
            }
        }
    }
    fun register(fullname: String, email: String, password: String, onResult: (Boolean, String) -> Unit = {_,_->}) {
        viewModelScope.launch {

            // check if user exists
            val existing = dao.getAccountByEmail(email)
            if (existing != null) {
                onResult(false, "Email already registered")
                return@launch
            }

            // insert user
            val account = Account(
                fullname = fullname,
                email = email,
                password = password  // you can hash later
            )

            dao.register(account)
            onResult(true, "Success")
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            // Asumsi: SessionManager punya fungsi getFullname() dan getEmail()
            // Jika menggunakan DataStore (Flow), gunakan .collect
            // Jika SharedPreferences (Sync), langsung ambil value

            // Contoh implementasi jika SessionManager menggunakan Flow (DataStore):
            session.getFullname().collect { name ->
                _currentName.value = name ?: "User"
            }
        }
        viewModelScope.launch {
            session.getEmail().collect { email ->
                _currentEmail.value = email ?: ""
            }
        }
    }

    suspend fun login(email: String, password: String): Account? {
        val account = dao.getAccountByEmail(email)
        return if (account != null && account.password == password){
            // SAVE SESSION HERE
            session.saveUserSession(
                email = account.email,
                fullname = account.fullname
            )
            loadUserData()
            account
        }
        else
            null
    }
}