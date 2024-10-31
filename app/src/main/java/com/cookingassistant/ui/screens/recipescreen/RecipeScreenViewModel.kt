package com.cookingassistant.ui.screens.recipescreen

import androidx.lifecycle.ViewModel
import com.cookingassistant.data.RecipeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeScreenViewModel : ViewModel() {
    private val _recipePlaceHolder = RecipeItem(1u,"","","","",
        "","","",0, listOf(),0,"","",
        "",listOf())
    private val _recipe = MutableStateFlow(_recipePlaceHolder)
    val recipe : StateFlow<RecipeItem> = _recipe

    private val _markedFavorite = MutableStateFlow(false)
    val markedFavorite : StateFlow<Boolean> = _markedFavorite

    private val _userRating = MutableStateFlow(0)
    val userRating : StateFlow<Int> = _userRating

    private val _userComment = MutableStateFlow("")
    val userComment : StateFlow<String> = _userComment

    fun loadRecipe(recipe : RecipeItem) {
        _recipe.value = recipe
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
        //todo send to database
    }

    fun onFavoriteChanged(isFavorite: Boolean) {
        //todo send to database
        _markedFavorite.value = isFavorite
    }
}