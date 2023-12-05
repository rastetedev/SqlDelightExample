package com.androidevhispano.sqldelightexample.data.product

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.androidevhispano.sqldelightexample.GetProductsSummary
import com.androidevhispano.sqldelightexample.Product
import com.androidevhispano.sqldelightexample.ProductQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductDataSourceImpl @Inject constructor(private val productQueries: ProductQueries) :
    ProductDataSource {

    override suspend fun insertOrUpdateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productQueries.insertOrReplaceProduct(product)
        }
    }

    override fun getProducts(): Flow<List<GetProductsSummary>> {
        return productQueries.getProductsSummary().asFlow().mapToList(Dispatchers.IO)
    }

    override fun getProduct(productId: Long): Product? {
       return productQueries.getProduct(productId).executeAsOneOrNull()
    }

    override suspend fun deleteProduct(productId: Long) {
        withContext(Dispatchers.IO) {
            productQueries.deleteProduct(productId)
        }
    }
}