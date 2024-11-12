package com.cookingassistant.ui.screens.reviews

import android.graphics.Bitmap
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

    private val _reviewImages = MutableStateFlow<MutableMap<Int, Bitmap?>>(mutableMapOf(0 to null))
    val reviewImages : StateFlow<MutableMap<Int, Bitmap?>> = _reviewImages

    fun loadReviews(recipeId: Int) {
        viewModelScope.launch {
            val result = _reviewService.getAllReviews(recipeId)
            if (result is Result.Success) {
                _reviews.value = result.data
            }
        }
    }

    fun LoadReviewsImage(reviews: List<ReviewGetDTO>?) {
        viewModelScope.launch {
            val newImagesMap = mutableMapOf<Int, Bitmap?>()
            reviews?.forEach { review ->
                val result = _reviewService.getReviewImageBitmap(review.id)
                if (result is Result.Success) {
                    newImagesMap[review.id] = result.data
                } else {
                    newImagesMap[review.id] = null // Set null if image loading fails
                }
            }
            _reviewImages.value = newImagesMap
        }
    }
}