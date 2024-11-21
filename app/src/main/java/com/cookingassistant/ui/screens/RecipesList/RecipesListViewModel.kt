package com.cookingassistant.ui.screens.RecipesList

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.DTO.RecipePageResponse
import com.cookingassistant.data.DTO.RecipeQuery
import com.cookingassistant.data.DTO.RecipeSimpleGetDTO
import com.cookingassistant.data.Models.Result
import com.cookingassistant.services.RecipeService
import com.cookingassistant.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class State{
    None,
    Search,
    Favourite,
    Own
}

class RecipesListViewModel(
    private val _service: RecipeService,
    private val userService: UserService,
    ): ViewModel() {

    private val _currentState : MutableStateFlow<State> = MutableStateFlow(State.None)
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

    private val tempRecipes : MutableStateFlow<List<RecipeSimpleGetDTO>> = MutableStateFlow(listOf())

    val currentState : StateFlow<State> = _currentState
    val recipeImages : StateFlow<MutableMap<Int,Bitmap?>> = _recipeImages
    val foundResults : StateFlow<Int> = _foundResults
    val currentPage : StateFlow<Int> = _currentPage
    val totalPages : StateFlow<Int> = _totalPages
    val recipes : StateFlow<List<RecipeSimpleGetDTO>> = _recipes
    val recipeQuery : StateFlow<RecipeQuery> = _recipeQuery
    val inputPageNumber : StateFlow<String> = _inputPageNumber

    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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

    fun setState(state: State){
        _currentState.value = state
    }

    private fun _selectPage(pageNumber: Int)  {
        if(pageNumber > 0 && pageNumber <= _totalPages.value) {
            if(_currentState.value == State.Search)
            {
                _onLoadQuery(_recipeQuery.value.copy(PageNumber = pageNumber))
            }
            else if(_currentState.value == State.Favourite)
            {
                var temp = (pageNumber - 1) * 10 + 10
                if(temp >= tempRecipes.value.size)
                {
                    temp = tempRecipes.value.size

                }
                _recipes.value = _recipes.value.subList((pageNumber - 1) * 10,temp)
                _currentPage.value = pageNumber
                loadImages()
            }
            else if(_currentState.value == State.Own)
            {
                var temp = (pageNumber - 1) * 10 + 10
                if(temp >= tempRecipes.value.size)
                {
                    temp = tempRecipes.value.size

                }
                _recipes.value = tempRecipes.value.subList((pageNumber - 1) * 10,temp)
                _currentPage.value = pageNumber
                loadImages()
            }
        }
    }

    fun deleteRecipe(recipeId : Int){
        viewModelScope.launch {
            var success = false
            try{
                val result = _service.deleteRecipe(recipeId)
                if(result is Result.Success){
                    success = true
                }
                else if(result is Result.Error){
                    Log.e("RecipeListViewModel", result.message)
                }
            }catch (e: Exception) {
                Log.e("RecipeListViewModel", e.message ?: "recipe couldnt be deleted")
            }
            finally {
                _isLoading.value = true
            }
        }
    }

    fun resetLoading(){
        _isLoading.value = false
    }

    fun loadImages(){
        viewModelScope.launch {
            for (r in recipes.value) {
                var bitmap : Bitmap? = null
                try {
                    val result = _service.getRecipeImageBitmap(r.id)
                    if(result is Result.Success && result.data != null)
                        bitmap=result.data
                    else if(result is Result.Error){
                        Log.e("_onLoadQuery", "Failed to get image: ${result.message}")
                        // TODO : ADD DEFAULT IMAGE IF FAILED
                        // bitmap = placeholder
                    }
                }
                catch (e: Exception) {
                    Log.e("RecipeListViewModelImages", e.message ?: "recipe id ${r.id} image couldn't be loaded", )
                }
                _recipeImages.value.apply { put(r.id, bitmap) }
            }
        }
    }

    fun loadOwnRecipes(){
        _currentState.value = State.Own
        _foundResults.value = 0
        viewModelScope.launch {
            var tag = "RecipeListViewModel"
            try {
                val result = userService.getUserRecipes()
                when(result) {
                    is Result.Success -> {
                        if(result.data != null) {
                            _recipeImages.value.apply { clear() }
                            _response.value = result.data
                            _totalPages.value = result.data.totalPages
                            tempRecipes.value = result.data.items
                            var temp = 10
                            if(temp >= tempRecipes.value.size)
                            {
                                temp = tempRecipes.value.size

                            }
                            _recipes.value = tempRecipes.value.subList(0,temp)
                            _currentPage.value = 1

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
                        val result = _service.getRecipeImageBitmap(r.id)
                        if(result is Result.Success && result.data != null)
                            bitmap=result.data
                        else if(result is Result.Error){
                            Log.e("_onLoadQuery", "Failed to get image: ${result.message}")
                            // TODO : ADD DEFAULT IMAGE IF FAILED
                            // bitmap = placeholder
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

    fun loadFavoriteRecipes(){
        _currentState.value = State.Favourite
        _foundResults.value = 0
        viewModelScope.launch {
            var tag = "RecipeListViewModel"
            try {
                val result = userService.getUserFavouriteRecipes()
                when(result) {
                    is Result.Success -> {
                        if(result.data != null) {
                            _recipeImages.value.apply { clear() }
                            _response.value = result.data
                            _totalPages.value = result.data.totalPages
                            tempRecipes.value = result.data.items
                            var temp = 10
                            if(temp >= tempRecipes.value.size)
                            {
                                temp = tempRecipes.value.size

                            }
                            _recipes.value = tempRecipes.value.subList(0,temp)
                            _currentPage.value = 1

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
                        val result = _service.getRecipeImageBitmap(r.id)
                        if(result is Result.Success && result.data != null)
                            bitmap=result.data
                        else if(result is Result.Error){
                            Log.e("_onLoadQuery", "Failed to get image: ${result.message}")
                            // TODO : ADD DEFAULT IMAGE IF FAILED
                            // bitmap = placeholder
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

    private fun _onLoadQuery(rq : RecipeQuery) {
        _currentState.value = State.Search
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
                        val result = _service.getRecipeImageBitmap(r.id)
                        if(result is Result.Success && result.data != null)
                            bitmap=result.data
                        else if(result is Result.Error){
                            Log.e("_onLoadQuery", "Failed to get image: ${result.message}")
                            // TODO : ADD DEFAULT IMAGE IF FAILED
                            // bitmap = placeholder
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