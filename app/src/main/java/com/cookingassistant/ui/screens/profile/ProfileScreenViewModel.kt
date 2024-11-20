package com.cookingassistant.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.cookingassistant.data.Models.Result
import com.cookingassistant.services.UserService
import com.cookingassistant.ui.screens.RecipesList.RecipesListViewModel
import kotlinx.coroutines.launch
import java.io.InputStream

class ProfileScreenViewModel(private val userService : UserService, val recipeListViewModel: RecipesListViewModel) : ViewModel() {
    private var _userProfilePicture by mutableStateOf<Bitmap?>(null)
    var userProfilePicture by mutableStateOf<Bitmap?>(null)

    fun loadFavoriteRecipes(){
        recipeListViewModel.loadFavoriteRecipes()
    }

    fun getUserProfilePicture() {
        viewModelScope.launch {
            var success = false
            try {
                val result = userService.getUserProfilePictureBitmap()
                if (result is Result.Success) {
                    _userProfilePicture = result.data
                    success = true
                } else if (result is Result.Error) {
                    Log.e("ProfileScreenViewModel", result.message)
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", e.message ?: "picture couldnt be loaded")
            }
            if(success && _userProfilePicture != null) {
                userProfilePicture = _userProfilePicture
            }
        }
    }

    fun addUserProfilePicture(imageStream: InputStream, mimeType: String){
        viewModelScope.launch {
            var success = false
            try {
                val result = userService.addUserProfilePicture(imageStream,mimeType)
                if (result is Result.Success) {
                    success = true
                } else if (result is Result.Error) {
                    Log.e("ProfileScreenViewModel", result.message)
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", e.message ?: "picture couldnt be loaded")
            }
        }
    }
}