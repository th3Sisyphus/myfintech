package com.example.myfintech.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.data.local.dao.CategorySpending
import com.example.myfintech.data.local.dao.TransactionDao
import com.example.myfintech.data.local.entities.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _avgIncome = MutableStateFlow(0f)
    val avgIncome: StateFlow<Float> = _avgIncome

    private val _avgExpense = MutableStateFlow(0f)
    val avgExpense: StateFlow<Float> = _avgExpense

    private val _totalIncome = MutableStateFlow(0f)
    val totalIncome: StateFlow<Float> = _totalIncome

    private val _totalExpense = MutableStateFlow(0f)
    val totalExpense: StateFlow<Float> = _totalExpense

    private val _expenseByCategory = MutableStateFlow<List<CategorySpending>>(emptyList())
    val expenseByCategory: StateFlow<List<CategorySpending>> = _expenseByCategory

    fun loadTransactions(email: String) {
        viewModelScope.launch {
            _transactions.value = transactionDao.getTransactionByEmail(email)
            _avgIncome.value = transactionDao.getAvgIncome(email) ?: 0f
            _avgExpense.value = transactionDao.getAvgExpense(email) ?: 0f
            _totalIncome.value = transactionDao.getTotalIncome(email) ?: 0f
            _totalExpense.value = transactionDao.getTotalExpense(email) ?: 0f
            _expenseByCategory.value = transactionDao.getExpenseByCategory(email)
        }
    }

    fun insertTransaction(email: String, type: String, amount: Float, title: String, category: String) {
        viewModelScope.launch {
            val transaction = Transaction(email = email, type = type, amount = amount, title = title, category = category)
            transactionDao.insertTransaction(transaction)
            loadTransactions(email) // Refresh the list
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.deleteTransaction(transaction)
            loadTransactions(transaction.email) // Refresh the list
        }
    }
}