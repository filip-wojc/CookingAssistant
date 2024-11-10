package com.cookingassistant.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.repositories.ApiRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

class UserService(private val _apiRepository: ApiRepository) {
    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING
    suspend fun addRecipeToUserFavourites(recipeId:Int){
        val response = _apiRepository.addRecipeToFavourites(recipeId)

    }

    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING
    suspend fun removeRecipeFromUserFavourites(recipeId: Int){
        val response = _apiRepository.removeRecipeFromFavourites(recipeId)

    }

    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING

    suspend fun getUserFavouriteRecipes(){
        val response = _apiRepository.getFavouriteRecipes()

    }

    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING
    suspend fun checkIfRecipeInUserFavourites(recipeId: Int) : Boolean? {
        return _apiRepository.checkIfRecipeInFavourites(recipeId).body()
    }

    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING
    suspend fun addUserProfilePicture(imageStream: InputStream, mimeType: String){
        val imageRequestBody = imageStream.readBytes()
            .toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("imageData", "profile.${mimeType.substringAfter("/")}", imageRequestBody)

        val response = _apiRepository.addProfilePicture(imagePart)
    }
    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING
    suspend fun getUserProfilePictureBitmap(): Bitmap?{
        val response = _apiRepository.getUserProfilePicture()

        if (response.isSuccessful && response.body() != null) {
            val inputStream: InputStream = response.body()!!.byteStream()
            return BitmapFactory.decodeStream(inputStream)

        } else {
            println("Failed to fetch image: ${response.code()} - ${response.message()}")
            return null
        }
    }

    // TODO: TEST
    // TODO: ADD RESPONSE HANDLING
    suspend fun deleteUserAccount(password: String){
        val passwordBody = password.toRequestBody()
        val response = _apiRepository.deleteAccount(passwordBody)
    }


}