package com.example.myfintech.ui.transaction.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfintech.data.local.database.DatabaseProvider
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.viewmodel.TransactionViewModel
import com.example.myfintech.data.local.entities.Transaction

import kotlinx.coroutines.launch

@Composable
fun Transactions(modifier: Modifier = Modifier) {

    // --- SESSION EMAIL ---
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val email by sessionManager.getEmail().collectAsState(initial = "")

    // --- DB + VIEWMODEL ---
    val db = remember { DatabaseProvider.getDatabase(context) }
    val transactionDao = remember { db.transactionDao() }
    val transactionViewModel = remember { TransactionViewModel(transactionDao) }

    // --- UI STATE ---
    var selectedFilter by remember { mutableStateOf("All") }
    var transactions by remember { mutableStateOf(emptyList<Transaction>()) }

    val scope = rememberCoroutineScope()

    // --- LOAD TRANSACTIONS ---
    LaunchedEffect(email) {
        if (!email.isNullOrEmpty()) {
            transactions = transactionViewModel.getTransaction(email?:"")
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color.White) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Header
            item {
                Column {
                    Text(
                        text = "Transactions",
                        color = Color(0xff0a0a0a),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Track all your money movements",
                        color = Color(0xff4a5565),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Search Bar
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xfff3f3f5))
                        .border(BorderStroke(1.dp, Color(0xFFE5E7EB)))
                        .padding(horizontal = 12.dp)
                ) {
                    Image(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Search transactions...",
                        color = Color(0xff717182),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Filter Buttons
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterButton(
                        text = "All",
                        isSelected = selectedFilter == "All",
                        onClick = { selectedFilter = "All" },
                        modifier = Modifier.weight(1f)
                    )
                    FilterButton(
                        text = "Income",
                        isSelected = selectedFilter == "Income",
                        onClick = { selectedFilter = "Income" },
                        modifier = Modifier.weight(1f)
                    )
                    FilterButton(
                        text = "Expense",
                        isSelected = selectedFilter == "Expense",
                        onClick = { selectedFilter = "Expense" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // --- FILTERED TRANSACTIONS LIST ---
            val filtered = when (selectedFilter) {
                "Income" -> transactions.filter { it.type == "income" }
                "Expense" -> transactions.filter { it.type == "expense" }
                else -> transactions
            }

            items(filtered) { t ->
                TransactionItem(
                    title = t.title,
                    category = t.category,
                    amount = if (t.type == "expense") "-Rp${t.amount}" else "+Rp${t.amount}",
                    date = "",// <---
                    amountColor = if (t.type == "expense") Color(0xffe7000b) else Color(0xff00a63e),
                    icon = if (t.type == "expense") Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    iconBackgroundColor = if (t.type == "expense") Color(0xffffe2e2) else Color(0xffdcfce7)
                )
            }
        }
    }
}

@Composable
private fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xffececf0) else Color.Transparent,
            contentColor = Color(0xff0a0a0a)
        ),
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
        modifier = modifier.height(36.dp)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun TransactionItem(
    title: String,
    category: String,
    amount: String,
    date: String,
    amountColor: Color,
    icon: ImageVector,
    iconBackgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(category, color = Color(0xff6a7282), fontSize = 14.sp)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(amount, color = amountColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(date, color = Color(0xff6a7282), fontSize = 14.sp)
        }
    }
}