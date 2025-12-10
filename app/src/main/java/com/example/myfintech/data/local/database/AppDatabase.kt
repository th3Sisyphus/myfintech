package com.example.myfintech.data.local.database
import com.example.myfintech.data.local.entities.*
import com.example.myfintech.data.local.dao.*


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Account::class, Transaction::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
}