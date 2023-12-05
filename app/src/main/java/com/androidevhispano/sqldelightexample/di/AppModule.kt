package com.androidevhispano.sqldelightexample.di

import android.content.Context
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.androidevhispano.sqldelightexample.CategoryQueries
import com.androidevhispano.sqldelightexample.Product
import com.androidevhispano.sqldelightexample.ProductQueries
import com.androidevhispano.sqldelightexample.ShopDatabase
import com.androidevhispano.sqldelightexample.data.category.CategoryDataSource
import com.androidevhispano.sqldelightexample.data.category.CategoryDataSourceImpl
import com.androidevhispano.sqldelightexample.data.product.ProductDataSource
import com.androidevhispano.sqldelightexample.data.product.ProductDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAndroidSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqliteDriver(ShopDatabase.Schema, context, "shop.db")
    }

    @Provides
    @Singleton
    fun providesDatabase(sqlDriver: SqlDriver): ShopDatabase {
        return ShopDatabase(
            sqlDriver,
            productAdapter = Product.Adapter(
                stockAdapter = IntColumnAdapter
            )
        )
    }

    @Provides
    @Singleton
    fun provideProductQueries(shopDatabase: ShopDatabase): ProductQueries {
        return shopDatabase.productQueries
    }

    @Provides
    @Singleton
    fun provideCategoryQueries(shopDatabase: ShopDatabase): CategoryQueries {
        return shopDatabase.categoryQueries
    }

    @Provides
    @Singleton
    fun provideProductDataSource(productQueries: ProductQueries): ProductDataSource {
        return ProductDataSourceImpl(productQueries)
    }

    @Provides
    @Singleton
    fun provideCategoryDataSource(categoryQueries: CategoryQueries): CategoryDataSource {
        return CategoryDataSourceImpl(categoryQueries)
    }
}