package com.cookingassistant.ui.screens.editor


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cookingassistant.ui.screens.editor.composables.DetailsPage
import com.cookingassistant.ui.screens.editor.composables.FrontPage
import com.cookingassistant.ui.screens.editor.composables.StepsPage


@Composable
fun EditorScreen(navController: NavController, viewModel: EditorScreenViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.observeAsState("front")
    var doItOnce by remember { mutableStateOf(true) }

    if(doItOnce) {
        viewModel.loadOccasions()
        viewModel.loadCategories()
        viewModel.loadDifficulties()
        viewModel.loadIngredients()
        viewModel.loadUnits()
        doItOnce = false
    }


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


