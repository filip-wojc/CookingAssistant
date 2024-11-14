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
import kotlinx.coroutines.flow.MutableStateFlow
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

    val difficultyOptions = MutableStateFlow<List<DifficultiesGetDTO>>(listOf())
    val categoryOptions = MutableStateFlow<List<CategoriesGetDTO>>(listOf())
    val occasionOptions = MutableStateFlow<List<OccasionsGetDTO>>(listOf())
    val ingredientsOptions = MutableStateFlow<List<String>>(listOf())
    val unitOptions = MutableStateFlow<List<String>>(listOf())

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    init {
        loadOccasions()
        loadCategories()
        loadDifficulties()
        loadIngredients()
        loadUnits()
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
                Log.e("EditorScreenViewModel", e.message ?: "recipe couldnt be created")
            }
        }
    }

    fun loadIngredients(){
        viewModelScope.launch {
            var success = false
            try{
                val result = recipeService.getAllIngredientsList()
                if(result is Result.Success){
                    ingredientsOptions.value = result.data ?: ingredientsOptions.value
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            }catch (e: Exception) {
                Log.e("EditorScreenViewModel", "ingredients couldnt be loaded")
            }
        }
    }

    fun loadUnits(){
        viewModelScope.launch {
            var success = false
            try{
                val result = recipeService.getAllUnitsList()
                if(result is Result.Success){
                    unitOptions.value = result.data ?: unitOptions.value
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("EditorScreenViewModel", result.message)
                }
            }catch (e: Exception) {
                Log.e("EditorScreenViewModel", "units couldnt be loaded")
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
                Log.e("EditorScreenViewModel", "difficulties couldnt be loaded")
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
                Log.e("EditorScreenViewModel", "categories couldnt be loaded")
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
                Log.e("EditorScreenViewModel", "occasions couldnt be loaded")
            }
        }
    }
}