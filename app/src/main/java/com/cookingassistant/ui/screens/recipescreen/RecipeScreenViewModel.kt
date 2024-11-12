package com.cookingassistant.ui.screens.recipescreen

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.Models.Result
import com.cookingassistant.data.DTO.ReviewPostDTO
import com.cookingassistant.services.RecipeService
import com.cookingassistant.services.ReviewService
import com.cookingassistant.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipeScreenViewModel(private val _recipeService: RecipeService, private val _userService : UserService, private val _reviewService: ReviewService) : ViewModel() {
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

    var ratingResponse = ""
/*
    fun loadRecipe(id : Int) {
        viewModelScope.launch {
            try {
                Log.i("RecipeScreenViewModel", "recipe details request sent")
                val response = _service.getRecipeDetails(id)
                if(response.isSuccessful) {
                    _recipe.value = response.body() ?: _recipe.value
                }
            } catch (e: Exception) {
                Log.e("RecipeScreenViewModel", "recipe couldnt be loaded")
            }
        }
    }
*/
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

    fun getIntRatings() {
        _recipe.value.ratings.roundToInt()
    }

    fun loadImage() {
        viewModelScope.launch {
            try {
                _recipeImg.value = _recipeService.getRecipeImageBitmap(_recipe.value.id) ?: _recipeImg.value
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
        viewModelScope.launch {
            try {
                val result = _reviewService.addReview(_recipe.value.id, ReviewPostDTO(_userRating.value, if(_userComment.value=="") null else _userComment.value))
                if(result is Result.Success) {
                    ratingResponse = "Rating successfully submitted"
                    _userComment.value = ""
                    _userRating.value = 0
                }
                else if (result is Result.Error) {
                    ratingResponse = "Can't submit review"
                    Log.e("onRatingSubmitted","result is error: ${result.message}")
                }
                else {
                    ratingResponse = "Unexpected server error"
                }
            }
            catch (e: Exception) {
                ratingResponse = e.message ?: "System failed to post your rating. We are sorry for inconvenience!"
                Log.e("RecipeScreenViewModel", "Failed to submit rating", )
            }
        }

    }

    fun onFavoriteChanged(isFavorite: Boolean) {
        //todo send to database remove from favorites
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