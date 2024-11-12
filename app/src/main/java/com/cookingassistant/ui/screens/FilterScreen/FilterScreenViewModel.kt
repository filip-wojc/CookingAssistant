package com.cookingassistant.ui.screens.FilterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.SearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilterScreenViewModel : ViewModel() {
    private val _filterByOccasion: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _filterByCategory: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _filterByDifficulty: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _suggestIngredientVisible : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _searchQuery : MutableStateFlow<String?> = MutableStateFlow(null)
    private val _suggestedIngredient : MutableStateFlow<String> = MutableStateFlow("")
    private val _addIngredientText : MutableStateFlow<String> = MutableStateFlow("")
    private val _selectedIngredients : MutableStateFlow<MutableList<String>> = MutableStateFlow(mutableListOf())
    private val _rollIngredients : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _sortBy : MutableStateFlow<String?> = MutableStateFlow(null)
    private val _sortDirection : MutableStateFlow<String?> = MutableStateFlow(null)

    val unrollIngredients : StateFlow<Boolean> = _rollIngredients
    val suggestedIngredient : StateFlow<String> = _suggestedIngredient
    val searchQuery: StateFlow<String?> = _searchQuery
    val addIngredientText : StateFlow<String> = _addIngredientText
    val selectedIngredients : StateFlow<MutableList<String>> = _selectedIngredients
    val filterByDifficulty : StateFlow<String?> = _filterByDifficulty
    val sortBy : StateFlow<String?> = _sortBy
    val sortDirection : StateFlow<String?> = _sortDirection

    private fun _onSortByChange(sort : String) {
        if(sort == "Default") {
            _sortBy.value = null
        } else {
            _sortBy.value = sort
        }
    }

    private fun _onSortDirectionChange(direction : String) {
        if(direction == "Default") {
            _sortBy.value = null
        } else {
            _sortBy.value = direction
        }
    }

    fun showIngredients() {
        _rollIngredients.value = false
    }

    fun hideIngredients() {
        _rollIngredients.value = true
    }

    fun onIngredientAdd(ingredient : String) {
        if(_selectedIngredients.value.contains(ingredient))
            return
        _selectedIngredients.value = _selectedIngredients.value.toMutableList().apply {
            add(ingredient)
        }
        _addIngredientText.value = ""
    }

    fun onIngredientRemove(ingredient: String) {
        _selectedIngredients.value = _selectedIngredients.value.toMutableList().apply {
            remove(ingredient)
        }
    }

    fun onAddIngredientTextChange(query : String) {
        if(query != "" && query.length >= (_addIngredientText.value.length)) {
            viewModelScope.launch(Dispatchers.IO) {
                _suggestedIngredient.value = SearchEngine.suggetIngredient(query)
            }
        }
        _addIngredientText.value = query
    }

    fun onSearchQueryChange(query: String) {
        if(query == "") {
            _searchQuery.value = null
        } else {
            _searchQuery.value = query
        }
    }

    private fun _onFilterByOccasionChange(occasion : String) {
        if(occasion == _filterByOccasion.value) {
            _filterByOccasion.value = null
        } else {
            _filterByOccasion.value = occasion
        }
    }

    private fun _onFilterByCategoryChange(category : String) {
        if(category == _filterByCategory.value) {
            _filterByCategory.value = null
        } else {
            _filterByCategory.value = category
        }
    }

    fun onFilterByDifficultyChange(difficulty : String) {
        if(difficulty == _filterByDifficulty.value) {
            _filterByDifficulty.value = null
        } else {
            _filterByDifficulty.value = difficulty
        }
    }

    fun onFilterChange(filterQuery : String, value: String) {
        when(filterQuery) {
            "Occasion" -> {_onFilterByOccasionChange(value)}
            "Category" -> {_onFilterByCategoryChange(value)}
            "SortBy" -> {_onSortByChange(value)}
            "SortDirection" -> {_onSortDirectionChange(value)}
            else -> {}
        }
    }

    fun getFilterValue(filterQuery : String) : String {
        when(filterQuery) {
            "Occasion" -> {return _filterByOccasion.value ?: ""}
            "Category" -> {return _filterByCategory.value ?: ""}
            else -> return ""
        }
    }

}