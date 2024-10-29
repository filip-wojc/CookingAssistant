package com.cookingassistant.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.LoginRequest
import com.cookingassistant.data.DTO.LoginResponse
import com.cookingassistant.data.DTO.RegisterRequest
import com.cookingassistant.data.TokenRepository
import com.cookingassistant.data.network.ApiRepository
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.InputStream

class UserService(private val apiRepository: ApiRepository) {
    suspend fun logInUser(email: String, password:String): Response<LoginResponse> {
        val requestBody = LoginRequest(email = email, password = password)
        return apiRepository.logIn(requestBody)
    }
    suspend fun registerUser(username: String, email: String, password: String): Response<String?>
    {
        val requestBody = RegisterRequest(username, email, password)
        return apiRepository.register(requestBody)
    }
    suspend fun getUserProfilePictureBitmap(): Bitmap?{
        val response = apiRepository.fetchUserProfilePicture()

        if (response.isSuccessful && response.body() != null) {
            val inputStream: InputStream = response.body()!!.byteStream()
            return BitmapFactory.decodeStream(inputStream)

        } else {
            println("Failed to fetch image: ${response.code()} - ${response.message()}")
            return null
        }
    }
}