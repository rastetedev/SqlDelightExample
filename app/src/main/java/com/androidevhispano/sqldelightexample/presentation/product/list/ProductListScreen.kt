package com.androidevhispano.sqldelightexample.presentation.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidevhispano.sqldelightexample.GetProductsSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    productViewModel: ProductViewModel = hiltViewModel(),
    navigateToCategories: () -> Unit,
    navigateToAddProduct: () -> Unit,
    navigateToEditProduct: (productId: Long) -> Unit
) {

    val products by productViewModel.products.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            var showMenu by remember { mutableStateOf(false) }

            TopAppBar(
                title = { Text(text = "Productos") },
                actions = {
                    IconButton(onClick = {
                        showMenu = true
                    }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                        }) {
                        DropdownMenuItem(
                            text = { Text(text = "Categories") },
                            onClick = {
                                showMenu = false
                                navigateToCategories()
                            }
                        )
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddProduct) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(products) { product ->
                ProductItem(productSummary = product) {
                    navigateToEditProduct(product.id)
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    productSummary: GetProductsSummary,
    onClick: (productId: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick(productSummary.id) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "ID", fontWeight = FontWeight.Medium)
            Text(text = "${productSummary.id}")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "NAME", fontWeight = FontWeight.Medium)
            Text(text = productSummary.name)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "PRICE", fontWeight = FontWeight.Medium)
            Text(text = "%.2f".format(productSummary.price))
        }
    }
}