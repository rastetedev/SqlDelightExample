package com.androidevhispano.sqldelightexample.presentation.product.save

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.channels.consumeEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    addProductViewModel: AddProductViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val categories by addProductViewModel.categories.collectAsState()

    LaunchedEffect(key1 = Unit) {
        addProductViewModel.onBackEvent.consumeEach {
            if (it) {
                navigateBack()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = "Agregar / Editar Producto") },
                actions = {
                    IconButton(onClick = addProductViewModel::deleteProduct) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = addProductViewModel::saveProduct) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "NAME", fontWeight = FontWeight.Medium)
                TextField(
                    value = addProductViewModel.productName.value,
                    onValueChange = addProductViewModel::updateName
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "STOCK", fontWeight = FontWeight.Medium)
                TextField(
                    value = "${addProductViewModel.productStock.value}",
                    onValueChange = addProductViewModel::updateStock,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "PRICE", fontWeight = FontWeight.Medium)
                TextField(
                    value = "${addProductViewModel.productPrice.value}",
                    onValueChange = addProductViewModel::updatePrice,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "ACTIVE", fontWeight = FontWeight.Medium)
                Switch(
                    checked = addProductViewModel.productIsActive.value,
                    onCheckedChange = addProductViewModel::updateIsActive
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var showCategoriesDropdown by remember { mutableStateOf(false) }

                Text(text = "CATEGORIES", fontWeight = FontWeight.Medium)
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showCategoriesDropdown = true
                        },
                    value = categories.find {
                        it.id == addProductViewModel.productCategoryId.value
                    }?.name ?: "",
                    onValueChange = {},
                    enabled = false
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = showCategoriesDropdown,
                    onDismissRequest = { showCategoriesDropdown = false }) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                addProductViewModel.updateCategoryId(category.id)
                                showCategoriesDropdown = false
                            }) {
                            Text(modifier = Modifier.fillMaxWidth(), text = category.name)
                        }
                    }
                }

            }

        }
    }
}