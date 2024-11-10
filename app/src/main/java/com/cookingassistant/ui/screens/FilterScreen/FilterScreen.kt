package com.cookingassistant.ui.screens.FilterScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cookingassistant.ui.composables.ExtendableList

@Preview
@Composable
fun FilterScreen(
    filterScreenViewModel: FilterScreenViewModel = viewModel()
) {
    var author : String =  ""
    Column(Modifier.padding(vertical = 10.dp)) {
        ExtendableList(
            title = "Occasion",
            listOf<String>("Dinner","One Pot Dish","Breakfast","Main Course","Brunch","Snack","Lunch","Dessert","Side Dish","Appetizer"),
            filterScreenViewModel,
            filterType = "Occasion",
            limit = 4
        )
        ExtendableList(
            title="Category",
            listOf<String>("Filip","Wójcik","Artur","Kozyra Course","Bruh","Robert","Lunch","Dessert","Side Dish","Appetizer"),
            filterScreenViewModel,
            filterType = "Category",
            limit = 6
        )
    }
}

