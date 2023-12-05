package com.androidevhispano.sqldelightexample.data.category

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.androidevhispano.sqldelightexample.Category
import com.androidevhispano.sqldelightexample.CategoryQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryDataSourceImpl @Inject constructor(private val categoryQueries: CategoryQueries) :
    CategoryDataSource {

    override suspend fun insertCategory(id: Long, name: String) {
        withContext(Dispatchers.IO) {
            categoryQueries.insertCategory(id, name)
        }
    }

    override suspend fun deleteCategory(id: Long) {
        withContext(Dispatchers.IO) {
            categoryQueries.deleteCategory(id)
        }
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryQueries.getAllCategories().asFlow().mapToList(Dispatchers.IO)
    }
}