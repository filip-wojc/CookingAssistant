package com.cookingassistant.ui.composables.topappbar

import android.util.Log
import androidx.lifecycle.ViewModel
import com.frosch2010.fuzzywuzzy_kotlin.FuzzySearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopAppBarViewModel : ViewModel() {
    private val _quickSearchText = MutableStateFlow("")
    val QuickSearchText : StateFlow<String> = _quickSearchText

    fun onSearchTextChanged(newText : String) {
        _quickSearchText.value = newText
        //val test =FuzzySearch.extractTop("this is string", listOf("is", "thiss","not the lol", "lmao"), 3)
        //Log.i("test",test.toString())
    }
}