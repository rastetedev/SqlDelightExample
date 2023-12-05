package com.androidevhispano.sqldelightexample.data.category

import com.androidevhispano.sqldelightexample.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {

    suspend fun insertCategory(id: Long, name: String)

    suspend fun deleteCategory(id: Long)

    fun getAllCategories() : Flow<List<Category>>
}