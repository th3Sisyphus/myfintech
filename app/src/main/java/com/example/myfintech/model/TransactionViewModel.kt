package com.example.myfintech.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.dao.TransactionDao
import com.example.myfintech.entity.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val dao: TransactionDao) : ViewModel() {
//    private val _transactionState = MutableStateFlow("")
//    val transactionState: StateFlow<String> = _transactionState.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    fun initTransaction() {
        viewModelScope.launch {
            _transactions.value = dao.getAllTransactions()
        }
    }

    fun insertTransaction(email: String, type: String, amount: Float, title: String, category: String, onResult: (Boolean, String) -> Unit = {_,_->}) {
        viewModelScope.launch {
            // insert user
            val transaction = Transaction(
                email = email,
                type = type,
                amount = amount,
                title = title,
                category = category
            )

            dao.insertTransaction(transaction)
            onResult(true, "Success")
        }
    }

    suspend fun getTransaction(email: String): List<Transaction> {
        return dao.getTransactionByEmail(email)
    }

    suspend fun getTotalIncome(email: String): Float{
        val transactions = dao.getTransactionByEmail(email)
        var totalIncome = 0f
        for (transaction in transactions){
            if (transaction.type == "income"){
                totalIncome += transaction.amount
            }
        }
        return totalIncome

    }

    suspend fun getTotalExpense(email: String): Float{
        val transactions = dao.getTransactionByEmail(email)
        var totalExpense = 0f
        for (transaction in transactions){
            if (transaction.type == "expense"){
                totalExpense += transaction.amount
            }
        }
        return totalExpense

    }

    suspend fun getTotalBalance(email: String): Float{
        val transactions = dao.getTransactionByEmail(email)
        var totalBalance = 0f
        for (transaction in transactions){
            if (transaction.type == "income"){
                totalBalance += transaction.amount
            } else {
                totalBalance -= transaction.amount
            }
        }
        return totalBalance
    }

    suspend fun getAVGIncome(email: String): Float{
        val transactions = dao.getTransactionByEmail(email)
        var totalIncome = 0f
        for (transaction in transactions){
            if (transaction.type == "income"){
                totalIncome += transaction.amount
            }
        }
        return totalIncome/transactions.size

    }

    suspend fun getAVGExpense(email: String): Float{
        val transactions = dao.getTransactionByEmail(email)
        var totalExpense = 0f
        for (transaction in transactions){
            if (transaction.type == "expense"){
                totalExpense += transaction.amount
            }
        }
        return totalExpense/transactions.size

    }

    suspend fun getExpenseByCategory(email: String): Map<String, Float> {
        val transactions = dao.getTransactionByEmail(email)

        val myMap = mutableMapOf(
            "Food" to 0f,
            "Transport" to 0f,
            "Shopping" to 0f,
            "Health" to 0f,
            "Salary" to 0f,
            "Others" to 0f
        )

        for (transaction in transactions) {
            if (transaction.type == "expense") {
                val category = transaction.category

                // Update only if category exists in map
                if (category in myMap) {
                    myMap[category] = myMap[category]!! + transaction.amount
                } else {
                    // If category not found, put inside "Others"
                    myMap["Others"] = myMap["Others"]!! + transaction.amount
                }
            }
        }

        return myMap
    }

    suspend fun getIncomeAndExpenses(email: String): Pair<Float, Float> {
        val transactions = dao.getTransactionByEmail(email)
        var income = 0f
        var expenses = 0f

        transactions.forEach {
            if (it.type == "income") income += it.amount
            if (it.type == "expense") expenses += it.amount
        }

        return Pair(income, expenses)
    }


}