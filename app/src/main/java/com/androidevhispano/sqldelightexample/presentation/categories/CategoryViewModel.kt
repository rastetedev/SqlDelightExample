package com.androidevhispano.sqldelightexample.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidevhispano.sqldelightexample.Category
import com.androidevhispano.sqldelightexample.data.category.CategoryDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryDataSource: CategoryDataSource) :
    ViewModel() {

    private val _categories: MutableStateFlow<List<Category>> =
        MutableStateFlow(emptyList())
    val categories = _categories.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categoryDataSource.getAllCategories().collectLatest {
                    withContext(Dispatchers.Main) {
                        _categories.value = it
                    }
                }
            }
        }
    }

    fun addCategory(categoryName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if(categoryName.isNotEmpty()){
                    categoryDataSource.insertCategory(System.currentTimeMillis(), categoryName)
                }
            }
        }
    }

    fun deleteCategory(categoryId: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categoryDataSource.deleteCategory(categoryId)
            }
        }
    }
}