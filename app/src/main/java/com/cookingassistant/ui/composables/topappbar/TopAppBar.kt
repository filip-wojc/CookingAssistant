package com.cookingassistant.ui.composables.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cookingassistant.ui.composables.bottomBorder
import kotlinx.coroutines.launch

@Preview
@Composable
fun testBar(){
    TopAppBar {  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(topAppBarviewModel : TopAppBarViewModel = viewModel(),
              searchQuery: String = "",
              content: @Composable() () -> Unit
              ) {

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val searchText by topAppBarviewModel.QuickSearchText.collectAsState()
    topAppBarviewModel.onSearchTextChanged(searchQuery)

    //---------//
    //Left menu//
    //---------//

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontSize = 40.sp,
                        text = "TOOLS",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )

            }
        }
    ) {

        //--------//
        //Top menu//
        //--------//

        androidx.compose.material3.Scaffold(
            topBar = {
                HorizontalDivider(modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .bottomBorder(10.dp,MaterialTheme.colorScheme.primary)
                )
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                    ,
                    colors = TopAppBarColors(containerColor = Color.Transparent,
                        actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
                        scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
                        navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
                        titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor
                    ),
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                    title = {
                        OutlinedTextField(
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            singleLine = true,
                            value = searchText,
                            onValueChange = {topAppBarviewModel.onSearchTextChanged(it)},
                            placeholder = { Text("search") },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Search, contentDescription = "Search",
                                )
                            }
                        )

                    },
                    navigationIcon = {
                        IconButton(
                            onClick =
                            {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open()
                                    }
                                }
                            },
                        )
                        {
                            Icon(Icons.Filled.Menu, contentDescription = "Left side menu")
                        }
                    },

                    actions = {
                        IconButton(onClick = { /* Handle action */ },

                        ) {
                            Icon(Icons.Filled.Person, contentDescription = "Profile")
                        }
                    }
                )
            },
            content = { padding ->
                val padd = padding
                content()
            },

        )

    }
}
