package com.cookingassistant.ui.screens.editor


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cookingassistant.ui.screens.editor.composables.DetailsPage
import com.cookingassistant.ui.screens.editor.composables.FrontPage
import com.cookingassistant.ui.screens.editor.composables.StepsPage


@Composable
fun EditorScreen(navController: NavController, viewModel: EditorScreenViewModel = viewModel()) {
    viewModel.loadOccasions()
    viewModel.loadCategories()
    viewModel.loadDifficulties()
    viewModel.loadIngredients()
    viewModel.loadUnits()

    val currentScreen by viewModel.currentScreen.observeAsState("front")

    when(currentScreen) {
        "front" -> {
            FrontPage(viewModel)
        }
        "details" -> {
            DetailsPage(viewModel)
        }
        "steps" -> {
            StepsPage(navController,viewModel)
        }
    }

}


