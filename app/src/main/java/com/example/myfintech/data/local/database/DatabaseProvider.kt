package com.example.myfintech.data.local.database

import android.content.Context
import androidx.room.Room
import com.example.myfintech.data.local.database.AppDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "fintechDB"
            ).fallbackToDestructiveMigration().build()

            INSTANCE = instance
            instance
        }
    }
}