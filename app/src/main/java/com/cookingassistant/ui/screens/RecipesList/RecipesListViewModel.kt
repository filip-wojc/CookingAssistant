package com.cookingassistant.ui.screens.RecipesList

import androidx.lifecycle.ViewModel
import com.cookingassistant.data.DTO.RecipeItemPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipesListViewModel: ViewModel() {
    private val items : MutableList<RecipeItemPreview> = mutableListOf()
    private val _recipes = MutableStateFlow(items)
    val recipes: StateFlow<MutableList<RecipeItemPreview>> = _recipes

    fun loadRecipes(newList : List<RecipeItemPreview>) {
        _recipes.value = newList.toMutableList()
    }
}