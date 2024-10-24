package com.cookingassistant.ui.composables.topappbar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cookingassistant.data.SearchEngine
import com.frosch2010.fuzzywuzzy_kotlin.FuzzySearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State

class TopAppBarViewModel : ViewModel() {
    private val _quickSearchText = MutableStateFlow("")
    private val _quickSearchResults = MutableStateFlow(listOf<String>())
    private  val _showSearchResults = MutableStateFlow(false)

    val QuickSearchText : StateFlow<String> = _quickSearchText
    val QuickSearchResults : StateFlow<List<String>> = _quickSearchResults
    val ShowSearchResults : StateFlow<Boolean> = _showSearchResults

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

    private fun _proposeResults(newText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quickSearchResults.value = SearchEngine.suggestRecipeNames(newText)
        }
    }
}