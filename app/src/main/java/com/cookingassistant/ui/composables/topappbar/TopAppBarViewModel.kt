package com.cookingassistant.ui.composables.topappbar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopAppBarViewModel : ViewModel() {
    private val _quickSearchText = MutableStateFlow("")
    val QuickSearchText : StateFlow<String> = _quickSearchText

    fun onSearchTextChanged(newText : String) {
        _quickSearchText.value = newText
    }
}