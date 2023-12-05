package com.androidevhispano.sqldelightexample.presentation.product.save

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidevhispano.sqldelightexample.Category
import com.androidevhispano.sqldelightexample.Product
import com.androidevhispano.sqldelightexample.data.category.CategoryDataSource
import com.androidevhispano.sqldelightexample.data.product.ProductDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val categoryDataSource: CategoryDataSource,
    private val productDataSource: ProductDataSource,
) :
    ViewModel() {

    private val productId: Long = savedStateHandle["productId"] ?: 0

    var productName: MutableState<String> = mutableStateOf("")
        private set

    var productStock: MutableState<String> = mutableStateOf("")
        private set

    var productPrice: MutableState<String> = mutableStateOf("")
        private set

    var productIsActive: MutableState<Boolean> = mutableStateOf(false)
        private set

    var productCategoryId: MutableState<Long> = mutableLongStateOf(0L)
        private set

    private val _categories: MutableStateFlow<List<Category>> =
        MutableStateFlow(emptyList())
    val categories = _categories.asStateFlow()

    val onBackEvent = Channel<Boolean>()

    init {
        loadProduct()
        loadCategories()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val product = productDataSource.getProduct(productId)
                withContext(Dispatchers.Main) {
                    product?.let {
                        updateName(it.name)
                        updateStock(it.stock.toString())
                        updatePrice(it.price.toString())
                        updateIsActive(it.isActive ?: false)
                        updateCategoryId(it.category_id)
                    }
                }

            }
        }
    }

    private fun loadCategories() {
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

    fun saveProduct() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productDataSource.insertOrUpdateProduct(
                    Product(
                        id = if (productId != 0L) productId else System.currentTimeMillis(),
                        name = productName.value,
                        stock = productStock.value.toInt(),
                        price = productPrice.value.toDouble(),
                        isActive = productIsActive.value,
                        category_id = productCategoryId.value
                    )
                )
                onBackEvent.send(true)
            }
        }
    }

    fun deleteProduct() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productDataSource.deleteProduct(savedStateHandle["productId"] ?: 0)
            }
            onBackEvent.send(true)
        }
    }

    fun updateName(name: String) {
        productName.value = name
    }

    fun updateStock(stock: String) {
        productStock.value = stock
    }

    fun updatePrice(price: String) {
        productPrice.value = price
    }

    fun updateIsActive(isActive: Boolean) {
        productIsActive.value = isActive
    }

    fun updateCategoryId(categoryId: Long) {
        productCategoryId.value = categoryId
    }

}