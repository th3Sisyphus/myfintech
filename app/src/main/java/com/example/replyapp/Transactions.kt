package com.example.replyapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun Transactions(modifier: Modifier = Modifier) {
    // State to keep track of the selected filter ("All", "Income", "Expense")
    var selectedFilter by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header ---
        item {
            Column {
                Text(
                    text = "Transactions",
                    color = Color(0xff0a0a0a),
                    lineHeight = 1.5.em,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Track all your money movements",
                    color = Color(0xff4a5565),
                    lineHeight = 1.5.em,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // --- Search Bar ---
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xfff3f3f5))
                    .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp)
            ) {
                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.requiredSize(20.dp)
                )
                Text(
                    text = "Search transactions...",
                    color = Color(0xff717182),
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // --- Filter Buttons ---
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
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

        // --- Transaction Items ---
        item {
            TransactionItem(
                title = "Coffee Shop",
                category = "Food & Drinks",
                amount = "-Rp53.000",
                date = "Oct 20",
                amountColor = Color(0xffe7000b),
                icon = Icons.Default.ArrowDownward, // Correctly passed as ImageVector
                iconBackgroundColor = Color(0xffffe2e2)
            )
        }
        item {
            TransactionItem(
                title = "Salary",
                category = "Income",
                amount = "+Rp50.000.000",
                date = "Oct 18",
                amountColor = Color(0xff00a63e),
                icon = Icons.Default.ArrowUpward, // Correctly passed as ImageVector
                iconBackgroundColor = Color(0xffdcfce7)
            )
        }
    }
}

/**
 * A reusable composable for the filter buttons ("All", "Income", "Expense").
 */
@Composable
fun FilterButton(
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
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp)
        )
    }
}

/**
 * A reusable composable to display a single transaction row.
 */
@Composable
fun TransactionItem(
    title: String,
    category: String,
    amount: String,
    date: String,
    amountColor: Color,
    icon: ImageVector, // CHANGED: from iconResId: Int
    iconBackgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)), RoundedCornerShape(14.dp))
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon
        Box(
            modifier = Modifier
                .requiredSize(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = icon, // CHANGED: from painter = painterResource(...)
                contentDescription = title,
                modifier = Modifier.requiredSize(20.dp)
            )
        }

        // Title and Category
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color(0xff0a0a0a),
                lineHeight = 1.5.em,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = category,
                color = Color(0xff6a7282),
                lineHeight = 1.43.em,
                style = TextStyle(fontSize = 14.sp)
            )
        }

        // Amount and Date
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                color = amountColor,
                textAlign = TextAlign.End,
                lineHeight = 1.5.em,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                text = date,
                color = Color(0xff6a7282),
                textAlign = TextAlign.End,
                lineHeight = 1.43.em,
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}

/**
 * This is the Preview function. It allows you to see your UI
 * in the Android Studio preview panel without running the app.
 */
@Preview(showBackground = true)
@Composable
fun TransactionsPreview() {
    // It's a good practice to wrap your preview in your app's theme
    MaterialTheme { // You might need to replace this with your actual app theme, e.g., ReplyAppTheme
        Transactions()
    }
}
