package com.androidevhispano.sqldelightexample.presentation.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidevhispano.sqldelightexample.GetProductsSummary
import com.androidevhispano.sqldelightexample.data.product.ProductDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor (private val productDataSource: ProductDataSource) : ViewModel() {

    private val _products: MutableStateFlow<List<GetProductsSummary>> =
        MutableStateFlow(emptyList())
    val products = _products.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            productDataSource.getProducts().collectLatest {
                _products.value = it
            }
        }
    }
}