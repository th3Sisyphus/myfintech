package com.example.myfintech.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfintech.data.local.dao.CategorySpending
import com.example.myfintech.data.local.database.DatabaseProvider
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.viewmodel.TransactionViewModel
import kotlin.math.max

@Composable
fun Analytic(modifier: Modifier = Modifier) {
    // --- SESSION EMAIL ---
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val email by sessionManager.getEmail().collectAsState(initial = null)

    // --- DB + VIEWMODEL ---
    val db = remember { DatabaseProvider.getDatabase(context) }
    val transactionDao = remember { db.transactionDao() }
    val transactionViewModel = remember { TransactionViewModel(transactionDao) }

    // --- UI STATE ---
    val avgIncome by transactionViewModel.avgIncome.collectAsState()
    val avgExpense by transactionViewModel.avgExpense.collectAsState()
    val expenseByCategory by transactionViewModel.expenseByCategory.collectAsState()
    val totalIncome by transactionViewModel.totalIncome.collectAsState()
    val totalExpense by transactionViewModel.totalExpense.collectAsState()

    // --- LOAD DATA ---
    LaunchedEffect(email) {
        email?.let { transactionViewModel.loadTransactions(it) }
    }

    Surface(
        color = Color(0xFFF3F4F6), // light gray background
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Your spending insights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
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
                        amount = avgIncome.toRupiahFormat(),
                        amountColor = Color(0xFF16A34A),
                        icon = Icons.Filled.TrendingUp,
                        iconBgColor = Color(0xFFD1FAE5),
                        modifier = Modifier.weight(1f)
                    )
                    AnalyticsInfoCard(
                        title = "Avg Expenses",
                        amount = avgExpense.toRupiahFormat(),
                        amountColor = Color(0xFFEF4444),
                        icon = Icons.Filled.TrendingDown,
                        iconBgColor = Color(0xFFFEE2E2),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                SpendingByCategoryCard(expenseByCategory)
            }
            item {
                IncomeVsExpensesCard(totalIncome, totalExpense)
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
private fun SpendingByCategoryCard(spendingData: List<CategorySpending>) {

    val categoryColors = mapOf(
        "Food" to Color(0xFF3B82F6),
        "Transport" to Color(0xFF8B5CF6),
        "Shopping" to Color(0xFFEC4899),
        "Health" to Color(0xFF10B981),
        "Salary" to Color(0xFFF59E0B),
        "Others" to Color(0xFF64748B)
    )

    val totalSpending = spendingData.sumOf { it.total.toDouble() }.toFloat()

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

        if (spendingData.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val diameter = size.minDimension
                    var startAngle = -90f

                    spendingData.forEach { spending ->
                        val segmentColor = categoryColors[spending.category] ?: Color.Gray
                        val percentage = if (totalSpending > 0) spending.total / totalSpending else 0f
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
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                spendingData.forEach { (name, amount) ->
                    CategoryRow(
                        label = name,
                        amount = amount.toRupiahFormat(),
                        color = categoryColors[name] ?: Color.Gray
                    )
                }
            }
        } else {
            Text("No spending data available.", modifier = Modifier.align(Alignment.CenterHorizontally))
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
private fun IncomeVsExpensesCard(totalIncome: Float, totalExpenses: Float) {

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

            // --- BAR CHART ---
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                val scale = size.height / chartMaxY
                val barWidth = size.width / 4

                // Income Bar
                drawRect(
                    color = Color(0xFF34D399),
                    topLeft = Offset(barWidth / 2, size.height - (totalIncome * scale)),
                    size = Size(barWidth, totalIncome * scale)
                )

                // Expense Bar
                drawRect(
                    color = Color(0xFFF87171),
                    topLeft = Offset(barWidth * 2.5f, size.height - (totalExpenses * scale)),
                    size = Size(barWidth, totalExpenses * scale)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticPreview() {
    Analytic()
}

fun Float.toRupiahFormat(): String {
    return "Rp${this.toInt()}"
}