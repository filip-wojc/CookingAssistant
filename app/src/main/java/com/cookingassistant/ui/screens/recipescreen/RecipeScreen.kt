package com.cookingassistant.ui.screens.recipescreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.cookingassistant.data.AdditionalFunctions
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import com.cookingassistant.ui.screens.recipescreen.composables.RecipeDetailsPage
import com.cookingassistant.ui.screens.recipescreen.composables.RecipeEndPage
import com.cookingassistant.ui.screens.recipescreen.composables.RecipeScreenFrontPage
import com.cookingassistant.ui.screens.recipescreen.composables.RecipeStepPage

@Composable
fun RecipeScreen(
    recipeScreenViewModel : RecipeScreenViewModel
) {
    val recipe by recipeScreenViewModel.recipe.collectAsState()
    val img by recipeScreenViewModel.recipeImg.collectAsState()

    // each step on separate page + 1 frontpage + 1 details
    val pagesCount by remember { mutableStateOf((recipe.steps?.size ?: 0) + 3 - 1) }
    var currentPage by remember { mutableStateOf(0) }
    var offsetX by remember { mutableStateOf(0f) }

    val sizeAnim1 by animateFloatAsState(
        targetValue = if (currentPage % 2 == 0) 1.0f else 0f
    )
    val sizeAnim2 by animateFloatAsState(
        targetValue = if (currentPage % 2 == 1) 1.0f else 0f
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 50.dp, horizontal = 5.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = {change, dragAmount ->
                        change.consume()
                        offsetX = dragAmount
                    },
                    onDragStart = {offsetX = 0f},
                    onDragEnd = {
                        if (offsetX < -5f && currentPage < pagesCount)
                            currentPage++
                        else if(offsetX > 5f && currentPage != 0)
                            currentPage--
                    }
                )
            }
    ) {
        Column (Modifier
            .fillMaxHeight(if(currentPage % 2 == 0) sizeAnim1 else sizeAnim2)
            .align(Alignment.Center)
        ){
            when(currentPage) {
                0 -> {
                    RecipeScreenFrontPage(recipe.name,img,recipe.description,recipe.authorName,recipe.categoryName,recipe.occasionName, recipe.difficultyName ?: "not known")
                }
                1 -> {
                    RecipeDetailsPage(recipe.caloricity, AdditionalFunctions.fancyTime(recipe.timeInMinutes),
                        AdditionalFunctions.tryGetCredit(recipe.id), AdditionalFunctions.fancyIngredients(recipe.ingredients), recipe.serves, modifier = Modifier.padding(vertical = 8.dp, horizontal = 5.dp))
                }
                in 2..<pagesCount -> {
                    RecipeStepPage(stepNumber = currentPage-1,
                        stepText = recipe.steps?.get(currentPage-2)?.description ?: "")
                }
                pagesCount -> {
                    RecipeEndPage(recipeScreenViewModel)
                }
            }
        }

        Row (
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxHeight(0.1f)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if(currentPage != 0) {
                IconButton(
                    onClick = {currentPage -= 1}
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardDoubleArrowLeft
                        ,null,
                        Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            if(currentPage >= 0 && currentPage < pagesCount) {
                IconButton(
                    onClick = {currentPage += 1}
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardDoubleArrowRight
                        ,null,
                        Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}