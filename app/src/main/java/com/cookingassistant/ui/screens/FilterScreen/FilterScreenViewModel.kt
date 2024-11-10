package com.cookingassistant.ui.screens.FilterScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterScreenViewModel : ViewModel() {
    private val _filterByOccasion: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _filterByCategory: MutableStateFlow<String?> = MutableStateFlow(null)
    val FilterByOccasion : StateFlow<String?> = _filterByOccasion
    val FilterByCategory: StateFlow<String?> = _filterByCategory

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

    fun onFilterChange(filterQuery : String, value: String) {
        when(filterQuery) {
            "Occasion" -> {_onFilterByOccasionChange(value)}
            "Category" -> {_onFilterByCategoryChange(value)}
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