package com.example.myfintech.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // Tambahkan ini
import com.example.myfintech.data.local.entities.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(account: Account)

    @Query("SELECT * FROM account_table WHERE email = :email LIMIT 1")
    suspend fun getAccountByEmail(email: String): Account?

    @Query("SELECT email FROM account_table")
    suspend fun getAllEmails(): List<String>

    @Update
    suspend fun updateAccount(account: Account)
}