package com.example.myfintech.data.local.database
import com.example.myfintech.data.local.entities.*
import com.example.myfintech.data.local.dao.*


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myfintech.data.local.entities.Category
import com.example.myfintech.data.local.dao.CategoryDao

@Database(
    entities = [Account::class, Transaction::class, Category::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}