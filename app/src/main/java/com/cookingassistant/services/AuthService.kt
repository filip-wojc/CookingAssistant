package com.cookingassistant.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.LoginRequest
import com.cookingassistant.data.DTO.LoginResponse
import com.cookingassistant.data.DTO.RegisterRequest
import com.cookingassistant.data.network.ApiRepository
import retrofit2.Response
import java.io.InputStream

class AuthService(private val _apiRepository: ApiRepository) {

    suspend fun logInUser(email: String, password:String): Response<LoginResponse> {
        val requestBody = LoginRequest(email = email, password = password)
        return _apiRepository.logIn(requestBody)
    }

    suspend fun registerUser(username: String, email: String, password: String): Response<String?>
    {
        val requestBody = RegisterRequest(username, email, password)
        return _apiRepository.register(requestBody)
    }





}