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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Analytics(modifier: Modifier = Modifier) {
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
                            amount = "Rp26.500.000",
                            amountColor = Color(0xFF16A34A),
                            icon = Icons.Filled.TrendingUp,
                            iconBgColor = Color(0xFFD1FAE5),
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsInfoCard(
                            title = "Avg Expenses",
                            amount = "Rp77.333",
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
    val categories = listOf(
        "Food & Drinks" to Pair(Color(0xFF3B82F6), 0.32f),
        "Transportation" to Pair(Color(0xFF8B5CF6), 0.05f),
        "Utilities" to Pair(Color(0xFFEC4899), 0.15f),
        "Entertainment" to Pair(Color(0xFFF59E0B), 0.28f),
        "Health" to Pair(Color(0xFF10B981), 0.20f)
    )

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

        // Pie Chart
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
                    val color = pair.first
                    val sweep = 360f * pair.second
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = diameter / 6)
                    )
                    startAngle += sweep
                }
            }
        }

        // Legend
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            CategoryRow("Food & Drinks", "Rp473.000", Color(0xFF3B82F6))
            CategoryRow("Transportation", "Rp40.000", Color(0xFF8B5CF6))
            CategoryRow("Utilities", "Rp150.000", Color(0xFFEC4899))
            CategoryRow("Entertainment", "Rp300.000", Color(0xFFF59E0B))
            CategoryRow("Health", "Rp200.000", Color(0xFF10B981))
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
    val months = listOf("Jun", "Jul", "Aug", "Sep", "Oct")
    val income = listOf(3500, 3000, 3200, 3400, 4600)
    val expenses = listOf(1500, 1200, 1300, 1100, 900)

    val chartMaxY = 5000f
    val yAxisLabels = listOf("5k", "4k", "3k", "2k", "1k", "0")
    val yGridLevels = listOf(5000f, 4000f, 3000f, 2000f, 1000f, 0f)

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
                .height(140.dp)
        ) {
            // Y
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

            // Bar chart Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                val scale = size.height / chartMaxY
                val gridLineColor = Color(0xFFE5E7EB)
                val gridLineStroke = 1.dp.toPx()
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                yGridLevels.forEach { level ->
                    val yPos = size.height - (level * scale)
                    drawLine(
                        color = gridLineColor,
                        start = Offset(x = 0f, y = yPos),
                        end = Offset(x = size.width, y = yPos),
                        strokeWidth = gridLineStroke,
                        pathEffect = if (level != 0f) pathEffect else null
                    )
                }

                val spacingPerGroup = size.width / months.size
                val barWidth = spacingPerGroup / 3f
                val groupPadding = (spacingPerGroup - (barWidth * 2f)) / 2f

                months.forEachIndexed { i, _ ->
                    val xGroupStart = i * spacingPerGroup
                    val xExpense = xGroupStart + groupPadding
                    val xIncome = xGroupStart + groupPadding + barWidth

                    val incHeight = income[i] * scale
                    val expHeight = expenses[i] * scale

                    // Expense Bar
                    drawRoundRect(
                        color = Color(0xFFEF4444),
                        topLeft = Offset(xExpense, size.height - expHeight),
                        size = Size(barWidth, expHeight),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                    // Income Bar
                    drawRoundRect(
                        color = Color(0xFF10B981),
                        topLeft = Offset(xIncome, size.height - incHeight),
                        size = Size(barWidth, incHeight),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                }
            }
        }

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem("expenses", Color(0xFFEF4444))
            Spacer(Modifier.width(24.dp))
            LegendItem("income", Color(0xFF10B981))
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