package com.cookingassistant.ui.screens.editor


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.DTO.*
import com.cookingassistant.data.Models.Result
import com.cookingassistant.services.RecipeService
import com.cookingassistant.util.ImageConverter
import kotlinx.coroutines.launch

class EditorScreenViewModel(private val recipeService: RecipeService) : ViewModel() {
    private val _currentScreen = MutableLiveData("front")
    val currentScreen: LiveData<String> = _currentScreen

    val imageConverter = ImageConverter()

    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var image by mutableStateOf<Uri?>(null)
    var serves by mutableStateOf<Int?>(null)
    var difficulty by mutableStateOf<DifficultiesGetDTO?>(null)
    var time by mutableStateOf<Int?>(null)
    var category by mutableStateOf<CategoriesGetDTO?>(null)
    var occasion by mutableStateOf<OccasionsGetDTO?>(null)
    var calories by mutableStateOf<Int?>(null)
    var ingredients by mutableStateOf(listOf<RecipeIngredientGetDTO>())
    var steps by mutableStateOf(listOf<String>())

    var difficultyOptions = mutableStateOf<List<DifficultiesGetDTO>>(listOf())
    var categoryOptions = mutableStateOf<List<CategoriesGetDTO>>(listOf())
    var occasionOptions = mutableStateOf<List<OccasionsGetDTO>>(listOf())
    var ingredientsOptions by mutableStateOf<List<String>>(listOf())
    var unitOptions by mutableStateOf<List<String>>(listOf())

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun checkRecipe():Boolean{
        if(name.isNotBlank() && description.isNotBlank()
            && image != null && serves != null
            && difficulty != null && time != null
            && category != null && occasion != null
            && calories != null && ingredients.size > 0
            && steps.size > 0){
            return true
        }
        return false
    }

    fun createRecipe(recipe: RecipePostDTO){
        viewModelScope.launch {
            var success = false
            try{
                val result = recipeService.addRecipe(recipe)
                if(result is Result.Success){
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            }catch (e: Exception) {
                Log.e("EditorScreenViewModel", e.message ?: "recipe couldnt be loaded")
            }
        }
    }

    fun loadIngredients(){
        viewModelScope.launch {
            var success = false
            try{
                val result = recipeService.getAllIngredientsList()
                if(result is Result.Success){
                    ingredientsOptions = result.data ?: ingredientsOptions
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            }catch (e: Exception) {
                Log.e("EditorScreenViewModel", "recipe couldnt be loaded")
            }
        }
    }

    fun loadUnits(){
        viewModelScope.launch {
            var success = false
            try{
                val result = recipeService.getAllUnitsList()
                if(result is Result.Success){
                    unitOptions = result.data ?: unitOptions
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            }catch (e: Exception) {
                Log.e("EditorScreenViewModel", "recipe couldnt be loaded")
            }
        }
    }

    fun loadDifficulties(){
        viewModelScope.launch {
            var success = false
            try {
                val result = recipeService.getAllDifficultiesList()
                if(result is Result.Success){
                    difficultyOptions.value = result.data ?: difficultyOptions.value
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            } catch (e: Exception) {
                Log.e("EditorScreenViewModel", "recipe couldnt be loaded")
            }
        }
    }

    fun loadCategories(){
        viewModelScope.launch {
            var success = false
            try {
                val result = recipeService.getAllCategoriesList()
                if(result is Result.Success){
                    categoryOptions.value = result.data ?: categoryOptions.value
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            } catch (e: Exception) {
                Log.e("EditorScreenViewModel", "recipe couldnt be loaded")
            }
        }
    }

    fun loadOccasions(){
        viewModelScope.launch {
            var success = false
            try {
                val result = recipeService.getAllOccasionsList()
                if(result is Result.Success){
                    occasionOptions.value = result.data ?: occasionOptions.value
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            } catch (e: Exception) {
                Log.e("EditorScreenViewModel", "recipe couldnt be loaded")
            }
        }
    }
}