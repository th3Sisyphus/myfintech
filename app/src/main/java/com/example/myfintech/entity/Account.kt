package com.example.myfintech.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_table")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullname: String,
    val email: String,
    val password: String
)
