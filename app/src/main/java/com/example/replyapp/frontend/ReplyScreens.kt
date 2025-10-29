package com.example.replyapp.frontend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.replyapp.data.Email


@Composable
fun ReplyListOnlyContent(
    replyUiState: ReplyUiState,
    onEmailClicked: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        items(replyUiState.emails) { email ->
            EmailListItem(email = email, onEmailClicked = { onEmailClicked(email) })
        }
    }
}


@Composable
fun ReplyListAndDetailContent(
    replyUiState: ReplyUiState,
    onEmailClicked: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(replyUiState.emails) { email ->
                EmailListItem(email = email, onEmailClicked = { onEmailClicked(email) })
            }
        }
        replyUiState.currentEmail?.let {
            EmailDetail(email = it, modifier = Modifier.weight(2f))
        }
    }
}

@Composable
fun EmailListItem(email: Email, onEmailClicked: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .clickable { onEmailClicked() }) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = email.sender, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = email.subject, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun EmailDetail(email: Email, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "From: ${email.sender}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Subject: ${email.subject}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = email.body, style = MaterialTheme.typography.bodyLarge)
    }
}