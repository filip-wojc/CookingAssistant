package com.cookingassistant.ui.composables.topappbar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

class TopAppBarViewModel(private val _service : RecipeService, private val _recipeScreenViewModel : RecipeScreenViewModel) : ViewModel() {
    private val _quickSearchText = MutableStateFlow("")
    private val _quickSearchResults = MutableStateFlow(listOf<ExtractedResult>())
    private  val _showSearchResults = MutableStateFlow(false)

    val QuickSearchText : StateFlow<String> = _quickSearchText
    val QuickSearchResults : StateFlow<List<ExtractedResult>> = _quickSearchResults
    val ShowSearchResults : StateFlow<Boolean> = _showSearchResults

    init {
        viewModelScope.launch {
            try {
                val status = _service.getRecipeNames()
                if (status.body() != null) {
                    SearchEngine.updateRecipesList(status.body()!!)
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

    fun onResultSubmited(id : Int) : Boolean {
        var isSuccess = false
        viewModelScope.launch {
            try {
                val response = _service.getRecipeDetails(id)
                if(response.isSuccessful) {
                    if(response.body() != null) {
                        _recipeScreenViewModel.loadRecipe(response.body()!!)
                        isSuccess = true
                    }
                }
                isSuccess = false
            } catch(e: Exception) {
                isSuccess = false
            }
        }
        return(isSuccess)
    }

    private fun _proposeResults(newText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quickSearchResults.value = SearchEngine.suggestRecipeNames(newText)
        }
    }
}