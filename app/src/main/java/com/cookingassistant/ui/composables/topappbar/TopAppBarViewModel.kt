package com.cookingassistant.ui.composables.topappbar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cookingassistant.data.SearchEngine
import com.cookingassistant.services.RecipeService
import com.cookingassistant.ui.screens.recipescreen.RecipeScreenViewModel
import com.frosch2010.fuzzywuzzy_kotlin.FuzzySearch
import com.frosch2010.fuzzywuzzy_kotlin.model.ExtractedResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State

class TopAppBarViewModel(private val _service : RecipeService, private val _recipeScreenViewModel : RecipeScreenViewModel, private val _navController: NavHostController) : ViewModel() {
    private val _quickSearchText = MutableStateFlow("")
    private val _quickSearchResults = MutableStateFlow(listOf<ExtractedResult>())
    private  val _showSearchResults = MutableStateFlow(false)

    val QuickSearchText : StateFlow<String> = _quickSearchText
    val QuickSearchResults : StateFlow<List<ExtractedResult>> = _quickSearchResults
    val ShowSearchResults : StateFlow<Boolean> = _showSearchResults

    init {
        _quickSearchResults.value = listOf<ExtractedResult>(ExtractedResult("Propositions are loading...",0,-1))
        viewModelScope.launch {
            try {
                val status = _service.getRecipeNames()
                if (status.body() != null) {
                    SearchEngine.updateRecipesList(status.body()!!)
                } else {
                    Log.e("TopAppBarViewModel", "_service.getRecipeNames().body is empty", )
                }
            } catch (e: Exception) {
                Log.e("TopAppBarViewModel", e.message ?: "couldnt get message names", )
            }
        }
    }

    fun updateRecipesList() {
        viewModelScope.launch {
            try {
                val status = _service.getRecipeNames()
                if (status.body() != null) {
                    SearchEngine.updateRecipesList(status.body()!!)
                } else {
                    Log.e("TopAppBarViewModel", "_service.getRecipeNames().body is empty", )
                }
            } catch (e: Exception) {
                Log.e("TopAppBarViewModel", e.message ?: "couldnt get message names", )
            }

        }
    }

    fun onSearchTextChanged(newText : String) {
        if(newText.trim() != "" && newText.length > _quickSearchText.value.length) {
            _proposeResults(newText)
            _showSearchResults.value = true
        }
        else if (newText.trim() == "") {
            _showSearchResults.value = false
        }
        _quickSearchText.value = newText
    }

    fun onResultsHide() {
        _showSearchResults.value = false;
    }

    fun onResultSubmited(id : Int) {
        viewModelScope.launch {
            try {
                val response = _service.getRecipeDetails(id)
                if(response.isSuccessful) {
                    if(response.body() != null) {
                        _recipeScreenViewModel.loadRecipe(response.body()!!)
                        _navController.navigate("recipeScreen")
                    }
                }
            } catch(e: Exception) {
                Log.e("onResultSubmited", e.message ?: "failed to submit result")
            }
        }
    }

    private fun _proposeResults(newText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quickSearchResults.value = SearchEngine.suggestRecipeNames(newText)
        }
    }
}