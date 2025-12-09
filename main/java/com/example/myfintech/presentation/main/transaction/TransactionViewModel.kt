package com.example.myfintech.presentation.main.transaction

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Placeholder ViewModel
@HiltViewModel
class TransactionViewModel @Inject constructor() : ViewModel() {
    // Logika untuk fetch daftar transaksi akan ada di sini
    val transactionList = listOf("Beli Kopi", "Bayar Listrik", "Gaji")
}