package com.example.myfintech.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val type: String, // expense/income
    val amount: Float,
    val title: String,
    val category: String
)