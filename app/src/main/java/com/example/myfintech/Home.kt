package com.example.myfintech

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfintech.ui.components.AddTransactionDialog


@Composable
fun Home(
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit
    ) {
    var showTransactionDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Header(
                        onProfileClicked = onProfileClicked
                    )
                }
                item {
                    BalanceCard()
                }
                item {
                    AddTransactionButton(
                        onClick = { showTransactionDialog = true }
                    )
                }
                item {
                    RecentTransactions()
                }
            }

            if (showTransactionDialog) {
                AddTransactionDialog(
                    onDismiss = { showTransactionDialog = false },
                    onAddTransaction = {
                        // TODO: Handle simpan data
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
                text = "Rainie Fanita",
                color = Color(0xff0a0a0a),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = onProfileClicked,
            modifier = Modifier.size(40.dp)
        ){
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
            ){
                Text(
                    text = "RF",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }


    }
}

@Composable
private fun BalanceCard() {
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
            .border(
                border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
                shape = cardShape
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = "Total Balance",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text("Total Balance", color = Color(0xffdbeafe), style = MaterialTheme.typography.bodyLarge)
        }
        Text(
            text = "Rp. 1.000.000,00",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoChip(
                icon = Icons.Filled.ArrowDownward,
                label = "Income",
                amount = "Rp 50.000.000",
                modifier = Modifier.weight(1f)
            )
            InfoChip(
                icon = Icons.Filled.ArrowUpward,
                label = "Expenses",
                amount = "Rp 1.630.000",
                modifier = Modifier.weight(1f)
            )
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
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Image(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(text = label, color = Color(0xffdbeafe), style = MaterialTheme.typography.bodySmall)
        }
        Text(text = amount, color = Color.White, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AddTransactionButton(
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color(0xff155dfc))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Transaction",
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Add Transaction",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RecentTransactions() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("See All", color = Color(0xff155dfc), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.clickable { /* TODO: Handle See All Click */ })
        }

        TransactionRow(
            icon = Icons.Filled.LocalCafe,
            title = "Coffee Shop",
            category = "Food & Drinks",
            amount = "-Rp53.000",
            amountColor = Color(0xffe7000b),
            iconBgColor = Color(0xffffe2e2)
        )
        TransactionRow(
            icon = Icons.Filled.AccountBalance,
            title = "Salary",
            category = "Income",
            amount = "+Rp50.000.000",
            amountColor = Color(0xff00a63e),
            iconBgColor = Color(0xffdcfce7),
            date = "Oct 18"
        )
        TransactionRow(
            icon = Icons.Filled.ShoppingCart,
            title = "Grocery Store",
            category = "Shopping",
            amount = "-Rp300.000",
            amountColor = Color(0xffe7000b),
            iconBgColor = Color(0xffffe2e2),
            date = "Oct 17"
        )
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
            modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBgColor)
        ) {
            Image(imageVector = icon, contentDescription = title, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(category, color = Color(0xff6a7282), style = MaterialTheme.typography.bodySmall)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(amount, color = amountColor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            if (date != null) {
                Text(date, color = Color(0xff6a7282), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 385, heightDp = 852)
@Composable
private fun HomePreview() {
    Home(Modifier,
        onProfileClicked = {})
}
