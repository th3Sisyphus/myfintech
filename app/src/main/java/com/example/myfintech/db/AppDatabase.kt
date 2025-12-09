package com.example.myfintech.db
import com.example.myfintech.entity.*
import com.example.myfintech.dao.*


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Account::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}
