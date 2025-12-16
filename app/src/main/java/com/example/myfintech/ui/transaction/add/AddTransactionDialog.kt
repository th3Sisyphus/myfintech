package com.example.myfintech.ui.transaction.add

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myfintech.data.local.database.DatabaseProvider
import com.example.myfintech.data.local.pref.SessionManager
import com.example.myfintech.viewmodel.TransactionViewModel
import com.example.myfintech.ui.theme.buttonGradient

// ML Kit Imports
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onAddTransaction: () -> Unit
){
    // State for form fields
    var isExpense by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }

    var category by remember { mutableStateOf("") }
    val categoryOptions = listOf("Food", "Transport", "Shopping", "Health", "Salary", "Others")
    var expanded by remember { mutableStateOf(false) }

    // Colors
    val inputGray = Color(0xFFF3F4F6)
    val textGray = Color(0xFF6B7280)
    val themeColor = Color(0xFF2B7FFF) // Warna utama aplikasi

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val email by sessionManager.getEmail().collectAsState(initial = "")

    // Database Setup
    val __db = remember { DatabaseProvider.getDatabase(context) }
    val __transactionDao = remember { __db.transactionDao() }
    val __transactionViewModel = remember { TransactionViewModel(__transactionDao) }

    // --- OCR LOGIC START ---
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { fileUri ->
            try {
                val image = InputImage.fromFilePath(context, fileUri)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        // Regex untuk mencari angka terbesar (Total Harga)
                        val numberRegex = Regex("""[\d,]+""")

                        val largestNum = numberRegex.findAll(visionText.text)
                            .map { it.value.replace(",", "") } // Hapus koma ribuan
                            .mapNotNull { it.toIntOrNull() }   // Ubah ke Int
                            .maxOrNull()                       // Ambil angka terbesar

                        if (largestNum != null) {
                            amount = largestNum.toString()
                            Toast.makeText(context, "Detected amount: $amount", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No amount detected", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to scan text", Toast.LENGTH_SHORT).show()
                        Log.e("OCR", "Error: ${e.message}")
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    // --- OCR LOGIC END ---

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // --- Header ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Transaction",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- Expense / Income Toggle ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFFE5E7EB))
                        .padding(4.dp)
                ) {
                    // Expense Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isExpense) Color.White else Color.Transparent)
                            .clickable { isExpense = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Expense",
                            color = if (isExpense) Color.Black else textGray,
                            fontWeight = if (isExpense) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    // Income Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (!isExpense) Color.White else Color.Transparent)
                            .clickable { isExpense = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Income",
                            color = if (!isExpense) Color.Black else textGray,
                            fontWeight = if (!isExpense) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- Amount Input ---
                Text("Amount", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text("Rp 0.00", color = textGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(inputGray),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputGray,
                        unfocusedContainerColor = inputGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // --- Scan Receipt Button (DIPERBAIKI) ---
                // Ditaruh tepat di bawah input amount agar rapi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End // Rata kanan
                ) {
                    TextButton(
                        onClick = { launcher.launch("image/*") }, // Hanya menerima gambar
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan",
                            tint = themeColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scan Receipt (OCR)",
                            color = themeColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Title Input ---
                Text("Title", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("What was this for?", color = textGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(inputGray),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputGray,
                        unfocusedContainerColor = inputGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- Category Dropdown ---
                Text("Category", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select a category", color = textGray) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(inputGray),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = inputGray,
                            unfocusedContainerColor = inputGray,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categoryOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    category = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Action Buttons ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel Button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Text("Cancel")
                    }

                    // Add Transaction Button
                    Button(
                        onClick = {
                            // Perbaikan validasi amount (menggunakan toFloatOrNull) agar tidak crash
                            if (amount.isNotEmpty()) {
                                __transactionViewModel.insertTransaction(
                                    email = email ?: "",
                                    type = if (isExpense) "expense" else "income",
                                    amount = amount.toFloatOrNull() ?: 0f,
                                    title = title,
                                    category = category.ifEmpty { "Others" }
                                )
                                onAddTransaction()
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(buttonGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Add Transaction", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionDialogPreview() {
    AddTransactionDialog(onDismiss = {}, onAddTransaction = {})
}