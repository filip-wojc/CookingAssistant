package com.cookingassistant.ui.screens.RecipesList

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.cookingassistant.data.DTO.RecipePageResponse
import com.cookingassistant.data.DTO.RecipeQuery
import com.cookingassistant.data.DTO.RecipeSimpleGetDTO
import com.cookingassistant.data.Models.Result
import com.cookingassistant.services.RecipeService
import com.cookingassistant.ui.screens.recipescreen.RecipeScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val _service:RecipeService,
    private val _recipeScreenViewModel:RecipeScreenViewModel,
    private val _navController : NavHostController
    ): ViewModel() {

    private val _currentPage : MutableStateFlow<Int> = MutableStateFlow(0)
    private val _totalPages : MutableStateFlow<Int> = MutableStateFlow(0)
    private val _recipes : MutableStateFlow<List<RecipeSimpleGetDTO>> = MutableStateFlow(listOf())
    private val _recipeQuery : MutableStateFlow<RecipeQuery> = MutableStateFlow(RecipeQuery())
    private val _response : MutableStateFlow<RecipePageResponse> = MutableStateFlow<RecipePageResponse>(
        RecipePageResponse(listOf(),0,0,0,0)
    )
    private val _foundResults : MutableStateFlow<Int> = MutableStateFlow(0) //0 - loading //1 - found // 2 - not found
    private val _inputPageNumber : MutableStateFlow<String> = MutableStateFlow("")
    private val _recipeImages = MutableStateFlow<MutableMap<Int,Bitmap?>>(mutableMapOf(0 to null))

    val recipeImages : StateFlow<MutableMap<Int,Bitmap?>> = _recipeImages
    val foundResults : StateFlow<Int> = _foundResults
    val currentPage : StateFlow<Int> = _currentPage
    val totalPages : StateFlow<Int> = _totalPages
    val recipes : StateFlow<List<RecipeSimpleGetDTO>> = _recipes
    val recipeQuery : StateFlow<RecipeQuery> = _recipeQuery
    val inputPageNumber : StateFlow<String> = _inputPageNumber

    fun onRecipeClick(recipeId : Int) {
        _recipeScreenViewModel.loadRecipe(recipeId)
        _navController.navigate("recipeScreen")
    }

    fun onInputPageNumberChange(number: String) {
        if(number.length < 10) {
            _inputPageNumber.value = number.filter { it.isDigit() }
        }
    }

    fun loadQuery(rq : RecipeQuery) {
        _onLoadQuery(rq)
    }

    fun onPageNumberSubmit() {
        if(_inputPageNumber.value.isNotEmpty()) {
            _selectPage(_inputPageNumber.value.toInt())
        }
    }

    fun onPageButtonClicked(number: Int) {
        _selectPage(_currentPage.value+number)
    }

    private fun _selectPage(pageNumber: Int)  {
        if(pageNumber > 0 && pageNumber <= _totalPages.value) {
            _onLoadQuery(_recipeQuery.value.copy(PageNumber = pageNumber))
        }
    }

    private fun _onLoadQuery(rq : RecipeQuery) {
        _foundResults.value = 0
        _recipeQuery.value = rq
        viewModelScope.launch {
            var tag = "RecipeListViewModel"
            try {
                val result = _service.findAllMatchingRecipes(rq)
                when(result) {
                    is Result.Success -> {
                        if(result.data != null) {
                            _recipeImages.value.apply { clear() }
                            _response.value = result.data
                            _totalPages.value = result.data.totalPages
                            _recipes.value = result.data?.items ?: listOf()
                            _currentPage.value = _recipeQuery.value.PageNumber
                            if(_recipes.value.size != 0) {
                                _foundResults.value = 1
                            } else {
                                _foundResults.value = 2
                            }
                        }
                        else {
                            _foundResults.value = 2
                            Log.w(tag, "_service.findAllMatchingRecipes(rq).body is empty", )
                        }
                    }
                    is Result.Error -> {
                        _foundResults.value = 2
                        Log.e(tag, result.message)
                    }
                    else -> {
                        _foundResults.value = 2
                        Log.e(tag, "Unexpected error occurred ${tag}")
                    }
                }
            } catch (e: Exception) {
                _foundResults.value = 2
                Log.e(tag, e.message ?: "couldn't get recipes", )
            }

            if(_foundResults.value == 1) { //try get images
                tag = "RecipeListViewModelImages"
                for (r in recipes.value) {
                    var bitmap : Bitmap? = null
                    try {
                        var result = _service.getRecipeImageBitmap(r.id)
                        if (result is Result.Success) {
                            bitmap = result.data
                        }
                    }
                    catch (e: Exception) {
                        Log.e(tag, e.message ?: "recipe id ${r.id} image couldn't be loaded", )
                    }
                    _recipeImages.value.apply { put(r.id, bitmap) }
                }
            }
        }
    }
}