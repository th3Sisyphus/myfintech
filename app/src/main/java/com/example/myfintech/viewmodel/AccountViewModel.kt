package com.example.myfintech.viewmodel


import com.example.myfintech.data.local.dao.*
import com.example.myfintech.data.local.database.*
import com.example.myfintech.data.local.pref.SessionManager
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.data.local.entities.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.myfintech.data.auth.GoogleAuthClient
import com.example.myfintech.data.local.dao.AccountDao
import com.example.myfintech.data.local.entities.Account
import android.content.Context

class AccountViewModel(private val dao: AccountDao, private val session: SessionManager,private val googleAuth: GoogleAuthClient ) : ViewModel() {

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

    fun signInWithGoogle(context: android.content.Context,onResult: (Boolean, String?) -> Unit){
        viewModelScope.launch {
            val (success, errorMessage) = googleAuth.signIn(context)

            if (success){
                val firebaseUser = googleAuth.getCurrentUser()
                firebaseUser?.let { user ->
                    val email = user.email ?: ""
                    val name = user.displayName ?: "Google User"

                    // Logika database tetap sama (Simpan ke Room)
                    val existingAccount = dao.getAccountByEmail(email)
                    if (existingAccount == null) {
                        val newAccount = Account(
                            fullname = name,
                            email = email,
                            password = ""
                        )
                        dao.register(newAccount)
                    }

                    session.saveUserSession(email, name)
                    loadUserData()
                    onResult(true, null)
                } ?: onResult(false, "Firebase user is null")
            }else{
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
    fun register(fullname: String, email: String, password: String, onResult: (Boolean, String) -> Unit = {_,_->}) {
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