package com.example.replyapp.frontend

import androidx.lifecycle.ViewModel
import com.example.replyapp.data.Email
import com.example.replyapp.data.EmailsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ReplyUiState(
    val emails: List<Email> = emptyList(),
    val currentEmail: Email? = null
)

class ReplyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReplyUiState())
    val uiState: StateFlow<ReplyUiState> = _uiState.asStateFlow()

    init {
        val emails = EmailsRepository.getEmails()
        _uiState.value = ReplyUiState(
            emails = emails,
            currentEmail = emails.firstOrNull()
        )
    }

    fun updateCurrentEmail(selectedEmail: Email) {
        _uiState.update { currentState ->
            currentState.copy(currentEmail = selectedEmail)
        }
    }
}