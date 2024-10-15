package com.cookingassistant

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class testViewModel(
    private val api: ApiService
): ViewModel() {
    val state = MutableStateFlow(emptyList<String>())

    init {
        viewModelScope.launch(Dispatchers.IO){
            val nutrients = api.getAllNutrients()
            state.value = nutrients
        }
    }
}