package com.cookingassistant.ui.composables.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideSource
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cookingassistant.compose.AppTheme
import com.cookingassistant.ui.composables.bottomBorder
import kotlinx.coroutines.launch

@Preview
@Composable
fun testBar(){
    AppTheme(true) {
        TopAppBar {  }
    }
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
    val searchResults by topAppBarviewModel.QuickSearchResults.collectAsState()
    val showResults by topAppBarviewModel.ShowSearchResults.collectAsState()

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
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 40.sp,
                        text = "TOOLS",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item", fontSize = 18.sp) },
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
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                )
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
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
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
                            textStyle = TextStyle.Default.copy(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer),
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
                Spacer(Modifier.padding(padding))
                content()
            },
        )
        if(showResults) {
            Box(Modifier.fillMaxWidth().padding(top = 75.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.7f)
                        .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                        .background(MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.5f))
                        .align(Alignment.Center).shadow(elevation = 2.dp)
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    for (result in searchResults) {
                        Text(text = result,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable {
                                topAppBarviewModel.onSearchTextChanged(result)
                                topAppBarviewModel.onResultsHide()
                            }
                                .padding(vertical = 5.dp, horizontal = 5.dp)
                        )
                        HorizontalDivider()
                    }
                    Row(Modifier.fillMaxWidth().clickable { topAppBarviewModel.onResultsHide() }
                        .background(MaterialTheme.colorScheme.inversePrimary)
                        .wrapContentWidth(Alignment.CenterHorizontally)

                    ) {
                        IconButton(onClick = { topAppBarviewModel.onResultsHide() }) {
                            Icon(
                                Icons.Filled.KeyboardArrowUp,
                                "Hide menu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
