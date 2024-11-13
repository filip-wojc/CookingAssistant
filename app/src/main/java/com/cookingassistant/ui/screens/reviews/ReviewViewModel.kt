package com.cookingassistant.ui.screens.reviews

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.cookingassistant.data.DTO.ReviewGetDTO
import com.cookingassistant.data.RecipeItemPreview
import com.cookingassistant.services.ReviewService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.DTO.Recipe
import com.cookingassistant.data.Models.Result
import com.cookingassistant.data.objects.ScreenControlManager
import com.cookingassistant.data.objects.SearchEngine

class ReviewViewModel(private val _reviewService: ReviewService) : ViewModel(){

    private val _reviews = MutableStateFlow<List<ReviewGetDTO>?>(emptyList())
    val reviews: StateFlow<List<ReviewGetDTO>?> = _reviews

    private val _reviewImages = MutableStateFlow<MutableMap<Int, Bitmap?>>(mutableMapOf(0 to null))
    val reviewImages : StateFlow<MutableMap<Int, Bitmap?>> = _reviewImages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loadingResult = MutableStateFlow<String>("")
    val loadingResult: StateFlow<String> = _loadingResult

    private val _currentUserReview = MutableStateFlow<ReviewGetDTO?>(ReviewGetDTO(
        id = -1,
        reviewAuthor = "Anonymous",
        value = 1,
        description = "No review available",
        dateCreated = "1970-01-01T00:00:00",
        dateModified = "1970-01-01T00:00:00"
    ))
    val currentUserReview: StateFlow<ReviewGetDTO?> = _currentUserReview

    fun loadReviews(recipeId: Int) {
        viewModelScope.launch {
            val result = _reviewService.getAllReviews(recipeId)
            if (result is Result.Success) {
                _reviews.value = result.data
            }
            LoadCurrentUserReview(recipeId)
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

    fun LoadCurrentUserReview(recipeId: Int) {
        viewModelScope.launch {
            val result = _reviewService.getMyReview(recipeId)
            if (result is Result.Success) {
                _currentUserReview.value = result.data
            }
            else {
                Log.e("User not found", "User not found for review")
            }
        }
    }

    fun DeleteReview(recipeId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true // Start loading
                val result = _reviewService.deleteReview(recipeId)
                if (result is Result.Success) {
                    _loadingResult.value = "Review deleted!"
                    _reviews.value = _reviews.value?.filter { it.id != _currentUserReview.value!!.id }
                    _isLoading.value = false
                }
                else if (result is Result.Error ) {
                    _isLoading.value = false
                    _loadingResult.value = "Delete failed: ${result.message}"
                    result.detailedErrors?.forEach { field, messages ->
                        messages.forEach { message ->
                            Log.d("login", "$field: $message")
                        }
                    }
                }
            }catch (e: Exception) {
                _loadingResult.value = "Delete failed: no access to server"
                _isLoading.value = false;
            }

        }
    }

}