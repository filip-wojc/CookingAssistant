package com.cookingassistant.ui.composables.topappbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LeftSideMenu(show : Boolean = false) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(topAppBarviewModel : TopAppBarViewModel = viewModel()) {
    val searchText by topAppBarviewModel.QuickSearchText.collectAsState()
    var showLeftMenu by remember {
        mutableStateOf(false)
    }

    androidx.compose.material3.Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                title = { },
                navigationIcon = {
                        IconButton(
                            onClick = { showLeftMenu = !showLeftMenu},

                            ) {
                            if (showLeftMenu) Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Menu") else Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {topAppBarviewModel.onSearchTextChanged(it)},
                            placeholder = { Text("search") },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Search, contentDescription = "Search Icon",
                                )
                            }
                        )
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(Icons.Filled.Person, contentDescription = "Favorite")
                        }
                    }
                }
            )
        },
        content = { padding ->
            val padd = padding
        },
    )
}