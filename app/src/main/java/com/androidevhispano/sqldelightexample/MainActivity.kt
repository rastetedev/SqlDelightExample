package com.androidevhispano.sqldelightexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androidevhispano.sqldelightexample.presentation.categories.CategoryListScreen
import com.androidevhispano.sqldelightexample.presentation.product.save.AddProductScreen
import com.androidevhispano.sqldelightexample.presentation.product.list.ProductListScreen
import com.androidevhispano.sqldelightexample.ui.theme.SqlDelightExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SqlDelightExampleTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "product_list_screen") {
                    composable(route = "product_list_screen") {
                        ProductListScreen(
                            navigateToCategories = {
                                navController.navigate("category_list_screen")
                            },
                            navigateToAddProduct = {
                                navController.navigate("product_add_screen")
                            },
                            navigateToEditProduct = {
                                navController.navigate("product_add_screen?productId=$it")
                            }
                        )
                    }
                    composable(route = "category_list_screen") {
                        CategoryListScreen(
                            navigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = "product_add_screen?productId={productId}", arguments = listOf(
                        navArgument("productId") {
                            defaultValue = 0L
                            type = NavType.LongType
                        }
                    )) {
                        AddProductScreen(
                            navigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}