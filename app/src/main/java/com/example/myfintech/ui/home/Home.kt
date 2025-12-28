package com.example.myfintech.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfintech.data.local.database.DatabaseProvider
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.data.local.entities.Transaction
import com.example.myfintech.viewmodel.TransactionViewModel
import com.example.myfintech.ui.transaction.add.AddTransactionDialog

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val email by sessionManager.getEmail().collectAsState(initial = null)

    // --- DB + VIEWMODEL (Single Instance) ---
    val db = remember { DatabaseProvider.getDatabase(context) }
    val transactionDao = remember { db.transactionDao() }
    val transactionViewModel = remember { TransactionViewModel(transactionDao) }

    // --- UI STATE (Collected from ViewModel) ---
    var showTransactionDialog by remember { mutableStateOf(false) }
    val transactions by transactionViewModel.transactions.collectAsState()
    val totalIncome by transactionViewModel.totalIncome.collectAsState()
    val totalExpense by transactionViewModel.totalExpense.collectAsState()
    val balance = totalIncome - totalExpense

    // --- LOAD DATA ---
    LaunchedEffect(email) {
        email?.let { transactionViewModel.loadTransactions(it) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { Header(onProfileClicked) }

                item { BalanceCard(balance, totalIncome, totalExpense) }

                item { AddTransactionButton { showTransactionDialog = true } }

                item { RecentTransactions(transactions.take(5)) } // Show top 5
            }

            if (showTransactionDialog) {
                AddTransactionDialog(
                    onDismiss = { showTransactionDialog = false },
                    onAddTransaction = {
                        // The ViewModel now automatically reloads the data upon insertion
                        showTransactionDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun Header(
    onProfileClicked: () -> Unit
) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val username by session.getFullname().collectAsState(initial = "")
    val userInitial = if (username.orEmpty().isNotBlank()) {
        username?.take(1)?.uppercase()
    } else {
        "U"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = "Welcome back,",
                color = Color(0xff4a5565),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = username?.substringBefore(" ") ?: "User",
                color = Color(0xff0a0a0a),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

        }

        IconButton(
            onClick = onProfileClicked,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            0f to Color(0xff2b7fff),
                            1f to Color(0xff9810fa)
                        )
                    )
            ) {
                Text(
                    text = userInitial ?: "U",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BalanceCard(balance: Float, income: Float, expense: Float) {
    val cardShape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(
                    0f to Color(0xff155dfc),
                    1f to Color(0xff9810fa)
                )
            )
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)), cardShape)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = "Total Balance",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(Modifier.width(8.dp))
            Text("Total Balance", color = Color(0xffdbeafe))
        }

        Text(
            text = "Rp. ${balance.toInt()}",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoChip(Icons.Filled.ArrowDownward, "Income", "Rp ${income.toInt()}", Modifier.weight(1f))
            InfoChip(Icons.Filled.ArrowUpward, "Expenses", "Rp ${expense.toInt()}", Modifier.weight(1f))
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, label: String, amount: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Spacer(Modifier.width(4.dp))
            Text(label, color = Color(0xffdbeafe))
        }
        Text(amount, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AddTransactionButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xff155dfc))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Transaction",
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Add Transaction",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RecentTransactions(transactions: List<Transaction>) {

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Transactions", fontWeight = FontWeight.Bold)
            Text(
                "See All",
                color = Color(0xff155dfc),
                modifier = Modifier.clickable { }
            )
        }

        if (transactions.isEmpty()) {
            Text("No transactions yet", color = Color.Gray)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                transactions.forEach { tx ->
                    TransactionRow(
                        icon =
                        if (tx.type == "income") Icons.Filled.ArrowUpward
                        else Icons.Filled.ArrowDownward,
                        title = tx.title,
                        category = tx.category,
                        amount = if (tx.type == "income") "+Rp${tx.amount.toInt()}" else "-Rp${tx.amount.toInt()}",
                        amountColor =
                        if (tx.type == "income") Color(0xFF00A63E)
                        else Color(0xFFE7000B),
                        iconBgColor =
                        if (tx.type == "income") Color(0xffdcfce7)
                        else Color(0xffffe2e2),
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(
    icon: ImageVector,
    title: String,
    category: String,
    amount: String,
    amountColor: Color,
    iconBgColor: Color,
    date: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBgColor)
        ) {
            Image(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(category, color = Color(0xff6a7282))
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(amount, color = amountColor, fontWeight = FontWeight.SemiBold)
            if (date != null) {
                Text(date, color = Color(0xff6a7282))
            }
        }
    }
}