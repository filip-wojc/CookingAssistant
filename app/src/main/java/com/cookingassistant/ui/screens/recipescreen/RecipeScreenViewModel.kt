package com.cookingassistant.ui.screens.recipescreen

import android.graphics.Bitmap
import android.util.Log
import androidx.constraintlayout.compose.Visibility
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.DTO.ReviewGetDTO
import com.cookingassistant.data.Models.Result
import com.cookingassistant.data.DTO.ReviewPostDTO
import com.cookingassistant.services.RecipeService
import com.cookingassistant.services.ReviewService
import com.cookingassistant.services.UserService
import com.cookingassistant.ui.screens.reviews.ReviewViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipeScreenViewModel(private val _recipeService: RecipeService,
                            private val _userService : UserService,
                            private val _reviewService: ReviewService,
                            private val _navController: NavHostController,
                            private val _reviewViewModel : ReviewViewModel) : ViewModel() {
    private val _recipePlaceHolder = RecipeGetDTO(1,"","","",0f,
        0,0, null, 0, "","",0,listOf(),
        listOf())
    private val _recipe = MutableStateFlow(_recipePlaceHolder)
    val recipe : StateFlow<RecipeGetDTO> = _recipe

    private val _markedFavorite = MutableStateFlow(false)
    val markedFavorite : StateFlow<Boolean> = _markedFavorite

    private val _userRating = MutableStateFlow(0)

    val userRating : StateFlow<Int> = _userRating

    private val _userComment = MutableStateFlow("")
    val userComment : StateFlow<String> = _userComment

    private val _recipeImg = MutableStateFlow(value=createBitmap(1,1))
    val recipeImg : StateFlow<Bitmap> = _recipeImg

    private val _showDialog = MutableStateFlow(false)
    val showDialog : StateFlow<Boolean> = _showDialog

    var ratingResponse = MutableStateFlow("")
    val reviewGetDto = MutableStateFlow<ReviewGetDTO?>(null)

    fun callDialog() {
        viewModelScope.launch {
            _showDialog.value = true
            delay(3000)
            _showDialog.value = false
        }
    }

    fun loadRecipe(id : Int) {
        viewModelScope.launch {
            var success = false
            try {
                val result = _recipeService.getRecipeDetails(id)
                if(result is Result.Success) {
                    _recipe.value = result.data ?: recipe.value
                    success = true
                }
                else if (result is Result.Error) {
                    Log.e("RecipeScreenViewModel", result.message)
                }
            } catch (e: Exception) {
                Log.e("RecipeScreenViewModel", "recipe couldnt be loaded")
            }
            if(success) {
                loadRecipe(_recipe.value)
            }
        }
    }

    fun onDeleteReview() {
        if(reviewGetDto.value != null && _reviewViewModel.currentUserReview.value != null) {
            _reviewViewModel.DeleteReview(_recipe.value.id)
            reviewGetDto.value = null
            _userComment.value = ""
            _userRating.value = 0
        }
    }

    fun onSeeReviews() {
        if(_recipe.value.id == -1) {
            return
        }
        _reviewViewModel.loadReviews(_recipe.value.id)
        userReviewMarkNull()
        _navController.navigate("recipeReviews")
        _reviewViewModel.loadReviews(_recipe.value.id)

    }

    fun loadRecipe(recipe : RecipeGetDTO) {
        _userComment.value = ""
        _userRating.value = 0
        _recipe.value = recipe
        loadImage()
        viewModelScope.launch {
            try {
                val result = _userService.checkIfRecipeInUserFavourites(recipe.id)
                if(result is Result.Success) {
                    _markedFavorite.value = result.data?: false
                }
                else if (result is Result.Error){
                    _markedFavorite.value = false
                    Log.e("RecipeScreenViewModel", "result is error when checking if recipe is in favourites")
                }

            } catch (e:Exception) {
                Log.e("RecipeScreenViewModel", "failed to check if recipe is in favorites: ${e.message}")
            }
        }
    }

    fun userReviewMarkNull() {
        reviewGetDto.value = null
    }

    fun loadImage() {
        viewModelScope.launch {
            try {
                val result = _recipeService.getRecipeImageBitmap(_recipe.value.id)
                if(result is Result.Success && result.data != null)
                    _recipeImg.value = result.data
                else if (result is Result.Error){
                    // TODO: ADD PLACEHOLDER IMAGE
                    Log.e("LoadImage()","Response error: ${result.message}")
                    result.detailedErrors?.forEach { field, messages ->
                        messages.forEach { message -> Log.e("Response error", "$field: $message") }
                    }
                }

            } catch (e: Exception) {
                Log.e("RecipeScreenViewModel", "recipe image couldnt be loaded")
            }
        }
    }

    fun onUserCommentChanged(newComment: String) {
        _userComment.value = newComment
    }

    fun onUserRatingChanged(newRating : Int) {
        _userRating.value = newRating
    }

    fun onRatingSubmitted(newRatingScore: Int, comment: String) {
        if(newRatingScore == 0) {
            return
            //dont submit rating score 0!
        }
        if(reviewGetDto.value != null) {
            if(userRating.value == 0 || userComment.value == "") {
                return
            }
            viewModelScope.launch {
                try {
                    val result = _reviewService.modifyReview(_recipe.value.id, ReviewPostDTO(_userRating.value, if(_userComment.value=="") null else _userComment.value))
                    if(result is Result.Success) {
                        _reviewViewModel.reviews.value?.forEach { review ->
                            if (review.id == reviewGetDto.value?.id) {
                                review.value = _userRating.value
                                review.description = _userComment.value
                            }
                        }
                        ratingResponse.value = "Rating successfully changed"
                    }
                    else if (result is Result.Error) {
                        Log.e("Modify Review","result is error: ${result.message}")
                    }
                }
                catch (e: Exception) {
                    Log.e("RecipeScreenViewModel","Modify failed: no access to server")
                }
            }
            callDialog()
            return
        }

        viewModelScope.launch {
            try {
                val result = _reviewService.addReview(_recipe.value.id, ReviewPostDTO(_userRating.value, if(_userComment.value=="") null else _userComment.value))
                if(result is Result.Success) {
                    ratingResponse.value = "Rating successfully submitted"
                }
                else if (result is Result.Error) {
                    ratingResponse.value = "You can't review your own recipe"
                    Log.e("onRatingSubmitted","result is error: ${result.message}")
                }
                else {
                    ratingResponse.value = "Unexpected server error"
                }
            }
            catch (e: Exception) {
                ratingResponse.value= e.message ?: "System failed to post your rating. We are sorry for inconvenience!"
                Log.e("RecipeScreenViewModel", "Failed to submit rating", )
            }
        }
        callDialog()
    }

    fun onUserEnterRecipeRatingPage() {
        if(_recipe.value.id == -1) {
            return
        }
        viewModelScope.launch {
            val result = _reviewService.getMyReview(_recipe.value.id)
            if (result is Result.Success) {
                reviewGetDto.value = result.data
                _userComment.value = reviewGetDto.value?.description ?: ""
                _userRating.value = reviewGetDto.value?.value ?: 0
            }
            else {
                Log.e("User not found", "User not found for review")
            }
        }
        _reviewViewModel.loadReviews(_recipe.value.id)

    }

    fun onFavoriteChanged(isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                if(isFavorite) {
                    _userService.addRecipeToUserFavourites(_recipe.value.id)
                } else {
                    _userService.removeRecipeFromUserFavourites(_recipe.value.id)
                }
            } catch (e: Exception) {
                Log.e("RecipeScreenViewModel", "Failed to save to favorites", )
            }
        }
        _markedFavorite.value = isFavorite
    }
}