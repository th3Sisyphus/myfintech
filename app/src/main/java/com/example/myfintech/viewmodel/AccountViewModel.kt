package com.example.myfintech.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myfintech.data.local.dao.*
import com.example.myfintech.data.local.database.*
import com.example.myfintech.data.local.pref.SessionManager


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.data.local.entities.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(private val dao: AccountDao, private val session: SessionManager) : ViewModel() {

    private val _emailState = MutableStateFlow("")
    val emailState: StateFlow<String> = _emailState.asStateFlow()

    private val _emails = MutableStateFlow<List<String>>(emptyList())
    val emails: StateFlow<List<String>> = _emails.asStateFlow()

    fun initEmail() {
        viewModelScope.launch {
            _emails.value = dao.getAllEmails()
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

    suspend fun login(email: String, password: String): Account? {
        val account = dao.getAccountByEmail(email)
        return if (account != null && account.password == password){
            // SAVE SESSION HERE
            session.saveUserSession(
                email = account.email,
                fullname = account.fullname
            )
            account
        }
        else
            null
    }
}