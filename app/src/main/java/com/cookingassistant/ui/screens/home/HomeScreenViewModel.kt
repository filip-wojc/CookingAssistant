package com.cookingassistant.ui.screens.home
import com.cookingassistant.util.ImageConverter
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.DTO.RecipePostDTO
import com.cookingassistant.services.RecipeService
import com.cookingassistant.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val _recipeService: RecipeService,private val _userService: UserService):ViewModel() {
    // TODO: DELETE THIS WHEN FINISHED TESTING
    private val _recipeImage = MutableStateFlow<Bitmap?>(null)
    private val _userProfileImage = MutableStateFlow<Bitmap?>(null)
    val recipeImage: StateFlow<Bitmap?> get() = _recipeImage
    val userProfileImage: StateFlow<Bitmap?> get() = _userProfileImage


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
// TODO : DELETE - test only
    fun fetchRecipeImage(recipeId:Int){
        viewModelScope.launch {
            val bitmap = _recipeService.getRecipeImageBitmap(recipeId)
            _recipeImage.value = bitmap
        }
    }
    // TODO : DELETE AFTER TEST
    fun getProfilePictureImageBitmap(){
        viewModelScope.launch{
            val bitmap = _userService.getUserProfilePictureBitmap()
            _userProfileImage.value = bitmap
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
                    // Handle nutrients list
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle any exception that occurs during the network call
            }
        }
    }



    // Test recipe post
    fun postRecipeWithImage(){
        // /storage/emulated/0/Download/ja.jpg

        viewModelScope.launch {
            try {
                val imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/ja.jpg"
                val imageConverter = ImageConverter()
                val imageByteArray = imageConverter.convertImageToByteArray(imagePath)
                val testRecipe = RecipePostDTO(
                    name = "Test Recipe",
                    description = "A delicious test recipe",
                    imageData = imageByteArray,
                    serves = 4,
                    difficulty = "Medium",
                    timeInMinutes = 45,
                    categoryId = 1,
                    ingredientNames = listOf("Flour", "Eggs", "Milk"),
                    ingredientQuantities = listOf("200", "3", "250"),
                    ingredientUnits = listOf("g", "pcs", "ml"),
                    steps = listOf("Mix the ingredients", "Bake for 30 minutes", "Let cool before serving"),
                    nutrientNames = listOf("Calories", "Protein", "Carbohydrates"),
                    nutrientQuantities = listOf("300", "10", "50"),
                    nutrientUnits = listOf("kcal", "g", "g")
                )
                // Call the suspend function from within a coroutine
                val response = _recipeService.addRecipe(testRecipe,imagePath)
                // Handle the response here (e.g., update UI state)
                if (response.isSuccessful) {
                    println("Poggers")

                    // TODO : ADD HANDLING
                    // Handle nutrients list
                } else {
                    println("NOT POGGERS")
                    // Handle error
                }
            } catch (e: Exception) {
                println("EXception:${e.message}")
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