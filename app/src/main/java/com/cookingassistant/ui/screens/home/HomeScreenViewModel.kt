package com.cookingassistant.ui.screens.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.services.RecipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val _recipeService: RecipeService):ViewModel() {
    // TODO: DELETE THIS WHEN FINISHED TESTING
    private val _recipeImage = MutableStateFlow<Bitmap?>(null)
    val recipeImage: StateFlow<Bitmap?> get() = _recipeImage


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

    fun getAllIngredientsList(){
        viewModelScope.launch {
            try {
                // Call the suspend function from within a coroutine
                val response = _recipeService.getAllIngredientsList()
                // Handle the response here (e.g., update UI state)
                if (response.isSuccessful) {
                    val ingredientsList = response.body()
                    // Handle nutrients list (for example, store it in a LiveData)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle any exception that occurs during the network call
            }
        }
    }

    fun fetchRecipeImage(recipeId:Int){
        viewModelScope.launch {
            val bitmap = _recipeService.getRecipeImageBitmap(recipeId)
            _recipeImage.value = bitmap
        }
    }
    fun getRecipeDetails(recipeId: Int){
        viewModelScope.launch {
            try {
                // Call the suspend function from within a coroutine
                val response = _recipeService.getRecipeDetails(recipeId)
                // Handle the response here (e.g., update UI state)
                if (response.isSuccessful) {
                    val recipe = response.body()
                    if(recipe != null){
                        // TODO: ADD LOGIC AND DELETE TESTING
                        printRecipeDetails(recipe)
                    }else{
                        println("No details available")
                    }
                    // TODO : ADD HANDLING
                    // Handle nutrients list (for example, store it in a LiveData)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle any exception that occurs during the network call
            }
        }
    }


// TODO : REMOVE AFTER FINISHED TESTING
    private fun printRecipeDetails(recipe: RecipeGetDTO?) {
        if (recipe != null) {
            println("Recipe Details:")
            println("ID: ${recipe.id}")
            println("Name: ${recipe.name}")
            println("Description: ${recipe.description}")
            println("Author: ${recipe.authorName}")
            println("Ratings: ${recipe.ratings}")
            println("Time In Minutes: ${recipe.timeInMinutes}")
            println("Serves: ${recipe.serves}")
            println("Difficulty: ${recipe.difficulty ?: "N/A"}")
            println("Vote Count: ${recipe.voteCount}")
            println("Category Name: ${recipe.categoryName}")

            // Print Ingredients
            println("\nIngredients:")
            recipe.ingredients?.forEachIndexed { index, ingredient ->
                println("Ingredient #${index + 1}")
                println("\tName: ${ingredient.ingredientName ?: "N/A"}")
                println("\tQuantity: ${ingredient.quantity ?: "N/A"}")
                println("\tUnit: ${ingredient.unit ?: "N/A"}")
            } ?: println("No Ingredients")

            // Print Nutrients
            println("\nNutrients:")
            recipe.nutrients?.forEachIndexed { index, nutrient ->
                println("Nutrient #${index + 1}")
                println("\tName: ${nutrient.nutrientName ?: "N/A"}")
                println("\tQuantity: ${nutrient.quantity ?: "N/A"}")
                println("\tUnit: ${nutrient.unit ?: "N/A"}")
            } ?: println("No Nutrients")

            // Print Steps
            println("\nSteps:")
            recipe.steps?.forEachIndexed { index, step ->
                println("Step #${step.stepNumber}")
                println("\tDescription: ${step.description ?: "N/A"}")
            } ?: println("No Steps")

        } else {
            println("Recipe is null")
        }
    }

}