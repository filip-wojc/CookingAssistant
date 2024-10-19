package com.cookingassistant.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.services.RecipeService
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val _recipeService: RecipeService):ViewModel() {
    fun getAllNutrientsList(){
        viewModelScope.launch {
            try {
                // Call the suspend function from within a coroutine
                val response = _recipeService.getAllNutrientsList()
                // Handle the response here (e.g., update UI state)
                if (response.isSuccessful) {
                    val nutrientsList = response.body()
                    // Handle nutrients list (for example, store it in a LiveData)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle any exception that occurs during the network call
            }
        }
    }

}