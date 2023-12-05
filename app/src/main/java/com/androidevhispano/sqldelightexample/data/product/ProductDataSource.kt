package com.androidevhispano.sqldelightexample.data.product

import com.androidevhispano.sqldelightexample.GetProductsSummary
import com.androidevhispano.sqldelightexample.Product
import kotlinx.coroutines.flow.Flow

interface ProductDataSource {

    suspend fun insertOrUpdateProduct(product: Product)

    fun getProducts() : Flow<List<GetProductsSummary>>

    fun getProduct(productId: Long) : Product?

    suspend fun deleteProduct(productId: Long)
}