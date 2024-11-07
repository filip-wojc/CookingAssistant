package com.cookingassistant.ui.screens.recipescreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cookingassistant.R
import com.cookingassistant.data.DTO.Recipe
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.DTO.ReviewGetDTO
import com.cookingassistant.data.DTO.ReviewPostDTO
import com.cookingassistant.data.RecipeItem
import com.cookingassistant.services.RecipeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RecipeScreenViewModel(private val _service: RecipeService, private val _recipeId : Int) : ViewModel() {
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

    init {
        viewModelScope.launch {
            try {
                val response = _service.getRecipeDetails(_recipeId)
                if(response.isSuccessful) {
                    _recipe.value = response.body() ?: _recipe.value
                }
            } catch (e: Exception) {
                Log.e("RecipeScreenViewModel", "recipe couldnt be loaded")
            }

        }
    }

    fun loadRecipe(recipe : RecipeGetDTO) {
        _recipe.value = recipe
    }

    fun getIntRatings() {
        _recipe.value.ratings.roundToInt()
    }

    fun loadImage() {
        viewModelScope.launch {
            try {
                _recipeImg.value = _service.getRecipeImageBitmap(_recipe.value.id) ?: _recipeImg.value
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

    fun onRatingSubmited(newRatingScore: Int, comment: String) {
        if(newRatingScore == 0) {
            return
            //dont submit rating score 0!
        }
        viewModelScope.launch {
            try {
                val response = _service.addReview(_recipe.value.id, ReviewPostDTO(_userRating.value, if(_userComment.value=="") null else _userComment.value))
                if(response.isSuccessful) {
                    _userComment.value = "Rating submited :) !"
                }
            }
            catch (e: Exception) {
                _userComment.value = e.message ?: "System failed to post your rating. We are sorry for inconvenience!"
                Log.e("RecipeScreenViewModel", "Failed to submit rating", )
            }
        }

    }

    fun onFavoriteChanged(isFavorite: Boolean) {
        //todo send to database
        viewModelScope.launch {
            try {
                _service.addRecipeToFavorites(_recipe.value.id)
            } catch (e: Exception) {
                Log.e("RecipeScreenViewModel", "Failed to save to favorites", )
            }
        }
        _markedFavorite.value = isFavorite
    }
}