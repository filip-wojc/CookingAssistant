package com.cookingassistant.ui.screens.reviews

import com.cookingassistant.data.DTO.ReviewGetDTO
import com.cookingassistant.data.RecipeItemPreview
import com.cookingassistant.services.ReviewService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.Models.Result

class ReviewViewModel(private val _reviewService: ReviewService) : ViewModel(){

    private val _reviews = MutableStateFlow<List<ReviewGetDTO>?>(emptyList())
    val reviews: StateFlow<List<ReviewGetDTO>?> = _reviews

    fun loadReviews(recipeId: Int) {
        viewModelScope.launch {
            val result = _reviewService.getAllReviews(recipeId)
            if (result is Result.Success) {
                _reviews.value = result.data
            }
        }
    }
}