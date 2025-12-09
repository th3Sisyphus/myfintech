package com.example.myfintech

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfintech.db.DatabaseProvider
import com.example.myfintech.db.SessionManager
import com.example.myfintech.entity.Transaction
import com.example.myfintech.model.TransactionViewModel
import kotlin.math.max

@Composable
fun Analytics(modifier: Modifier = Modifier) {
    // --- SESSION EMAIL ---
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val email by sessionManager.userEmail.collectAsState(initial = "")

    // --- DB + VIEWMODEL ---
    val db = remember { DatabaseProvider.getDatabase(context) }
    val transactionDao = remember { db.transactionDao() }
    val transactionViewModel = remember { TransactionViewModel(transactionDao) }

    // --- UI STATE ---
    var selectedFilter by remember { mutableStateOf("All") }
    var transactions by remember { mutableStateOf(emptyList<Transaction>()) }
    var avgIncome by remember { mutableStateOf(0f) }
    var avgExpense by remember { mutableStateOf(0f) }


    val scope = rememberCoroutineScope()

    // --- LOAD TRANSACTIONS ---
    LaunchedEffect(email) {
        if (!email.isNullOrEmpty()) {
            transactions = transactionViewModel.getTransaction(email?:"")
            avgIncome = transactionViewModel.getAVGIncome(email?:"")
            avgExpense = transactionViewModel.getAVGExpense(email?:"")

        }
    }

    Surface(
        color = Color(0xFFF3F4F6), // light gray background
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                item {
                    Column {
                        Text(
                            text = "Analytics",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Your spending insights",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnalyticsInfoCard(
                            title = "Avg Income",
                            amount = "Rp${avgIncome}",
                            amountColor = Color(0xFF16A34A),
                            icon = Icons.Filled.TrendingUp,
                            iconBgColor = Color(0xFFD1FAE5),
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsInfoCard(
                            title = "Avg Expenses",
                            amount = "Rp${avgExpense}",
                            amountColor = Color(0xFFEF4444),
                            icon = Icons.Filled.TrendingDown,
                            iconBgColor = Color(0xFFFEE2E2),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    SpendingByCategoryCard()
                }
                item {
                    IncomeVsExpensesCard()
                }
            }
        }
    }
}

@Composable
private fun AnalyticsInfoCard(
    title: String,
    amount: String,
    amountColor: Color,
    icon: ImageVector,
    iconBgColor: Color,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = amountColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )
        }
        Text(
            text = amount,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}

@Composable
private fun SpendingByCategoryCard() {
    // --- SESSION EMAIL ---
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val email by sessionManager.userEmail.collectAsState(initial = "")

    // --- DB + VIEWMODEL ---
    val db = remember { DatabaseProvider.getDatabase(context) }
    val transactionDao = remember { db.transactionDao() }
    val transactionViewModel = remember { TransactionViewModel(transactionDao) }

    // --- UI STATE ---
    var spendingByCategory by remember { mutableStateOf(emptyMap<String, Float>()) }

    // --- LOAD DATA ---
    LaunchedEffect(email) {
        if (!email.isNullOrEmpty()) {
            spendingByCategory = transactionViewModel.getExpenseByCategory(email ?: "")
        }
    }

    // COLORS MATCH YOUR DATABASE CATEGORY KEYS
    val categoryColors = mapOf(
        "Food" to Color(0xFF3B82F6),
        "Transport" to Color(0xFF8B5CF6),
        "Shopping" to Color(0xFFEC4899),
        "Health" to Color(0xFF10B981),
        "Salary" to Color(0xFFF59E0B),
        "Others" to Color(0xFF64748B)
    )

    // --- DYNAMIC CATEGORY LIST FOR PIE CHART ---
    val totalSpending = spendingByCategory.values.sum()

    val categories = spendingByCategory.map { (name, amount) ->
        val color = categoryColors[name] ?: Color.Gray
        val percentage = if (totalSpending > 0) amount / totalSpending else 0f
        name to Pair(color, percentage)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Spending by Category",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        // --- PIE CHART ---
        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = size.minDimension
                var startAngle = -90f

                categories.forEach { (_, pair) ->
                    val segmentColor = pair.first
                    val percentage = pair.second
                    val sweep = 360f * percentage

                    drawArc(
                        color = segmentColor,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = diameter / 6)
                    )
                    startAngle += sweep
                }
            }
        }

        // --- LEGEND ---
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            spendingByCategory.forEach { (name, amount) ->
                CategoryRow(
                    label = name,
                    amount = "Rp${amount.toInt()}",
                    color = categoryColors[name] ?: Color.Gray
                )
            }
        }
    }
}

@Composable
private fun CategoryRow(label: String, amount: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF374151)
            )
        }
        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827)
        )
    }
}


@Composable
private fun IncomeVsExpensesCard() {

    // --- SESSION EMAIL ---
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val email by sessionManager.userEmail.collectAsState(initial = "")

    // --- DB + VIEWMODEL ---
    val db = remember { DatabaseProvider.getDatabase(context) }
    val transactionDao = remember { db.transactionDao() }
    val transactionViewModel = remember { TransactionViewModel(transactionDao) }

    // --- UI STATE ---
    var totalIncome by remember { mutableStateOf(0f) }
    var totalExpenses by remember { mutableStateOf(0f) }

    // --- LOAD DATA ---
    LaunchedEffect(email) {
        if (!email.isNullOrEmpty()) {
            val allData = transactionViewModel.getIncomeAndExpenses(email ?: "")
            totalIncome = allData.first
            totalExpenses = allData.second
        }
    }

    // --- CHART RANGE ---
    val chartMaxY = max(totalIncome, totalExpenses).coerceAtLeast(1f)
    val yGridLevels = listOf(chartMaxY, chartMaxY * 0.75f, chartMaxY * 0.5f, chartMaxY * 0.25f, 0f)
    val yAxisLabels = yGridLevels.map { (it / 1000).toInt().toString() + "k" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Income vs Expenses",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {

            // --- Y AXIS ---
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                yAxisLabels.forEach {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            // --- BAR CHART (Single Comparison) ---
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                val scale = size.height / chartMaxY
                val gridColor = Color(0xFFE5E7EB)
                val gridStroke = 1.dp.toPx()
                val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                // Grid
                yGridLevels.forEachIndexed { index, level ->
                    val yPos = size.height - (level * scale)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, yPos),
                        end = Offset(size.width, yPos),
                        strokeWidth = gridStroke,
                        pathEffect = if (index != yGridLevels.lastIndex) dash else null
                    )
                }

                val barWidth = size.width / 4f
                val spacing = size.width / 4f

                // EXPENSE BAR
                val expenseHeight = totalExpenses * scale
                drawRoundRect(
                    color = Color(0xFFEF4444),
                    topLeft = Offset(spacing, size.height - expenseHeight),
                    size = Size(barWidth, expenseHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )

                // INCOME BAR
                val incomeHeight = totalIncome * scale
                drawRoundRect(
                    color = Color(0xFF10B981),
                    topLeft = Offset(spacing * 2 + barWidth, size.height - incomeHeight),
                    size = Size(barWidth, incomeHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }
        }

        // --- LEGEND ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem("Expenses", Color(0xFFEF4444))
            Spacer(Modifier.width(24.dp))
            LegendItem("Income", Color(0xFF10B981))
        }
    }
}


@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            color = Color(0xFF374151),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 850)
@Composable
private fun PreviewAnalyticsPage() {
    MaterialTheme {
        Analytics()
    }
}