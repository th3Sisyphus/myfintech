package com.example.myfintech.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.data.local.dao.CategoryDao
import com.example.myfintech.data.local.entities.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryDao: CategoryDao) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val defaultCategories = listOf("Food", "Transport", "Shopping", "Health", "Salary", "Others")

    fun getCategories(email: String) {
        viewModelScope.launch {
            val userCategories = categoryDao.getCategories(email)
            if (userCategories.isEmpty()) {
                defaultCategories.forEach { categoryName ->
                    val category = Category(name = categoryName, email = email)
                    categoryDao.insertCategory(category)
                }
                _categories.value = categoryDao.getCategories(email)
            } else {
                _categories.value = userCategories
            }
        }
    }

    fun addCategory(name: String, email: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                val newCategory = Category(name = name, email = email)
                categoryDao.insertCategory(newCategory)
                getCategories(email)
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            if (!defaultCategories.contains(category.name)) {
                categoryDao.deleteCategory(category)
                getCategories(category.email)
            }
        }
    }
}