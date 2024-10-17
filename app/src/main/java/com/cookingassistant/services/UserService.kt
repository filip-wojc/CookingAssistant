package com.cookingassistant.services

import com.cookingassistant.data.DTO.LoginRequest
import com.cookingassistant.data.DTO.LoginResponse
import com.cookingassistant.data.network.ApiRepository
import retrofit2.Response

class UserService(private val api: ApiRepository) {
    suspend fun logInUser(email: String, password:String): Response<LoginResponse> {
        val request = LoginRequest(email = email, password = password)
        return api.logIn(request)
    }
    // TODO: finish registration
    // suspend fun registerUser(username: String, email: String, password: String): Response<>
}