package com.androidevhispano.sqldelightexample.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidevhispano.sqldelightexample.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val categories by categoryViewModel.categories.collectAsState()
    var showAddCategoryModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = "Categorias") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddCategoryModal = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(categories) { category ->
                CategoryItem(category = category) {
                    categoryViewModel.deleteCategory(it)
                }
            }
        }

        if (showAddCategoryModal) {

            var categoryName by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddCategoryModal = false },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(size = 16.dp) // shape of the dialog
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {


                    Text(
                        text = "Agrega una categoria",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = categoryName,
                        onValueChange = {
                            categoryName = it
                        })

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            categoryViewModel.addCategory(categoryName)
                            showAddCategoryModal = false
                        }) {
                        Text(text = "Agregar categoría")
                    }

                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onLongClick: (categoryId: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick(category.id)
                    }
                )
            }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "ID", fontWeight = FontWeight.Medium)
            Text(text = "${category.id}")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "NAME", fontWeight = FontWeight.Medium)
            Text(text = category.name)
        }
    }
}