package com.example.replyapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Analyticspage(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xfff9fafb) // Use as the main background color
    ) {
        // Use LazyColumn to make the content scrollable
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            item {
                HeaderSection()
            }

            // Average Income and Expenses Cards
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AnalyticsInfoCard(
                        title = "Avg Income",
                        amount = "Rp26.500.000",
                        amountColor = Color(0xff00a63e),
                        icon = Icons.Filled.TrendingUp,
                        iconBgColor = Color(0xffdcfce7),
                        modifier = Modifier.weight(1f)
                    )
                    AnalyticsInfoCard(
                        title = "Avg Expenses",
                        amount = "Rp.77.333",
                        amountColor = Color(0xffe7000b),
                        icon = Icons.Filled.TrendingDown,
                        iconBgColor = Color(0xffffe2e2),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Spending by Category Card
            item {
                SpendingByCategoryCard()
            }

            // Income vs Expenses Chart Card
            item {
                IncomeVsExpensesChart()
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column {
        Text(
            text = "Analytics",
            color = Color(0xff0a0a0a),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your spending insights",
            color = Color(0xff4a5565),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun AnalyticsInfoCard(
    title: String,
    amount: String,
    amountColor: Color,
    icon: ImageVector,
    iconBgColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconBgColor)
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(amountColor)
                )
            }
            Text(
                text = title,
                color = Color(0xff4a5565),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = amount,
            color = amountColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SpendingByCategoryCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Spending by Category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        // This is where a real pie chart would go. Using a placeholder icon.
        Image(
            imageVector = Icons.Filled.PieChart,
            contentDescription = "Pie Chart Placeholder",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            colorFilter = ColorFilter.tint(Color(0xff3b82f6))
        )
        // Category spending details
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CategorySpendingRow(label = "Food & Drinks", amount = "Rp473.000", color = Color(0xff3b82f6))
            CategorySpendingRow(label = "Transportation", amount = "Rp40.000", color = Color(0xff8b5cf6))
            CategorySpendingRow(label = "Utilities", amount = "Rp150.000", color = Color(0xffec4899))
            CategorySpendingRow(label = "Entertainment", amount = "Rp300.000", color = Color(0xfff59e0b))
            CategorySpendingRow(label = "Health", amount = "Rp200.000", color = Color(0xff10b981))
        }
    }
}

@Composable
fun CategorySpendingRow(label: String, amount: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = amount, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun IncomeVsExpensesChart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)), RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Income vs Expenses",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        // This is where a real bar chart would go. Using a placeholder icon.
        Image(
            imageVector = Icons.Filled.BarChart,
            contentDescription = "Bar Chart Placeholder",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.CenterHorizontally),
            colorFilter = ColorFilter.tint(Color.LightGray)
        )
        // Chart legend
        // Chart legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            // CORRECTED: Use Arrangement.spacedBy to add space between items,
            // and add the alignment to center them as a group.
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChartLegendItem(label = "Income", color = Color(0xff10b981))
            ChartLegendItem(label = "Expenses", color = Color(0xffef4444))
        }

    }
}

@Composable
fun ChartLegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnalyticspagePreview() {
    Analyticspage()
}
