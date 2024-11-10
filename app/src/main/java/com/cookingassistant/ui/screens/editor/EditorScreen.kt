package com.cookingassistant.ui.screens.editor


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cookingassistant.ui.screens.editor.composables.DetailsPage
import com.cookingassistant.ui.screens.editor.composables.FrontPage
import com.cookingassistant.ui.screens.editor.composables.StepsPage

@Preview
@Composable
fun EditorScreen() {
    val navController = rememberNavController()
    val viewModel: EditorScreenViewModel = viewModel()

    NavHost(navController = navController, startDestination = "steps") {
        composable("front") { FrontPage(navController,viewModel) }
        composable("details") { DetailsPage(navController,viewModel) }
        composable("steps") { StepsPage(navController,viewModel) }
    }
}


