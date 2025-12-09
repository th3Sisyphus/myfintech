package com.example.myfintech.dao
import com.example.myfintech.entity.*
import androidx.room.*

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(account: Account)

    @Query("SELECT * FROM account_table WHERE email = :email LIMIT 1")
    suspend fun getAccountByEmail(email: String): Account?

    @Query("SELECT email FROM account_table")
    suspend fun getAllEmails(): List<String>
}