package com.cookingassistant.ui.screens.recipescreen

import androidx.lifecycle.ViewModel
import com.cookingassistant.data.RecipeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeScreenViewModel : ViewModel() {
    private val _recipePlaceHolder = RecipeItem(1u,"","","","",
        "","","",0, listOf(),0,"","",
        "",listOf())
    private val _recipe = MutableStateFlow(_recipePlaceHolder)
    val recipe : StateFlow<RecipeItem> = _recipe

    fun loadRecipe(recipe : RecipeItem) {
        _recipe.value = recipe
    }
}