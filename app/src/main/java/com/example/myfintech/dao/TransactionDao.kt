package com.example.myfintech.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfintech.entity.Transaction

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table WHERE email = :email")
    suspend fun getTransactionByEmail(email: String): List<Transaction>

    @Query("SELECT * FROM transaction_table")
    suspend fun getAllTransactions(): List<Transaction>
}