package com.cookingassistant.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.RecipePageResponse
import com.cookingassistant.data.DTO.UserDeleteRequest
import com.cookingassistant.data.DTO.UserPasswordChangeDTO
import com.cookingassistant.data.repositories.ApiRepository
import com.cookingassistant.data.Models.Result
import com.cookingassistant.util.ApiResponseParser
import com.cookingassistant.util.ImageConverter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

class UserService(private val _apiRepository: ApiRepository) {

    private val _apiResponseParser = ApiResponseParser()
    private val _imageConverter = ImageConverter()
    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun addRecipeToUserFavourites(recipeId:Int):Result<Unit?>{
        val response = _apiRepository.addRecipeToFavourites(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun removeRecipeFromUserFavourites(recipeId: Int):Result<Unit?>{
        val response = _apiRepository.removeRecipeFromFavourites(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getUserFavouriteRecipes():Result<RecipePageResponse?>{
        val response = _apiRepository.getFavouriteRecipes()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }


    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun checkIfRecipeInUserFavourites(recipeId: Int):Result<Boolean?> {
        val response = _apiRepository.checkIfRecipeInFavourites(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    suspend fun checkIfRecipeIsCreatedByUser(recipeId: Int):Result<Boolean?> {
        val response = _apiRepository.checkIfRecipeIsCreatedByUser(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun addUserProfilePicture(imageStream: InputStream, mimeType: String):Result<Unit?>{
        val imageRequestBody = imageStream.readBytes()
            .toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("imageData", "profile.${mimeType.substringAfter("/")}", imageRequestBody)

        val response = _apiRepository.addProfilePicture(imagePart)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }
    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getUserProfilePictureBitmap(): Result<Bitmap?>{
        val response = _apiRepository.getUserProfilePicture()
        if(response.isSuccessful && response.body() != null) {
            val imageByteArray = response.body()!!.bytes()
            val bitmap = _imageConverter.convertByteArrayToBitmap(imageByteArray)

            return Result.Success(bitmap)
        }
        else{
            // TODO: CHANGE LATER
            return Result.Error("Failed to fetch user image:${response.message()}", errorCode = response.code())
        }

    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun changeUserPassword(oldPassword: String, newPassword:String , newPasswordConfirm:String ):Result<Unit?>{
        val passwordChangeDTO = UserPasswordChangeDTO(
            oldPassword,
            newPassword,
            newPasswordConfirm
        )
        val response = _apiRepository.changePassword(passwordChangeDTO)
        val result= _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun deleteUserAccount(password: String):Result<Unit?>{
        val userDeleteRequest = UserDeleteRequest(password)
        val response = _apiRepository.deleteUser(userDeleteRequest)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }


}