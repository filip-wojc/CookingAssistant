package com.cookingassistant.services

import com.cookingassistant.data.DTO.LoginRequest
import com.cookingassistant.data.DTO.LoginResponse
import com.cookingassistant.data.DTO.RegisterRequest
import com.cookingassistant.data.TokenRepository
import com.cookingassistant.data.network.ApiRepository
import retrofit2.Response

class UserService(private val apiRepository: ApiRepository) {
    suspend fun logInUser(email: String, password:String): Response<LoginResponse> {
        val requestBody = LoginRequest(email = email, password = password)
        return apiRepository.logIn(requestBody)
    }
    // TODO: finish registration
    suspend fun registerUser(username: String, email: String, password: String): Response<String?>
    {
        val requestBody = RegisterRequest(username, email, password)
        return apiRepository.register(requestBody)
    }
}