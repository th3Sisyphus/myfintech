package com.example.myfintech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfintech.data.local.entities.*

// Helper data class for category spending query
data class CategorySpending(
    val category: String,
    val total: Float
)

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table WHERE email = :email")
    suspend fun getTransactionByEmail(email: String): List<Transaction>

    @Query("SELECT * FROM transaction_table")
    suspend fun getAllTransactions(): List<Transaction>

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT AVG(amount) FROM transaction_table WHERE email = :email AND type = 'income'")
    suspend fun getAvgIncome(email: String): Float?

    @Query("SELECT AVG(amount) FROM transaction_table WHERE email = :email AND type = 'expense'")
    suspend fun getAvgExpense(email: String): Float?

    @Query("SELECT category, SUM(amount) as total FROM transaction_table WHERE email = :email AND type = 'expense' GROUP BY category")
    suspend fun getExpenseByCategory(email: String): List<CategorySpending>

    @Query("SELECT SUM(amount) FROM transaction_table WHERE email = :email AND type = 'income'")
    suspend fun getTotalIncome(email: String): Float?

    @Query("SELECT SUM(amount) FROM transaction_table WHERE email = :email AND type = 'expense'")
    suspend fun getTotalExpense(email: String): Float?
}