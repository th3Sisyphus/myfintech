package com.example.myfintech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfintech.data.local.entities.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE email = :email")
    suspend fun getCategories(email: String): List<Category>

    @Delete
    suspend fun deleteCategory(category: Category)
}