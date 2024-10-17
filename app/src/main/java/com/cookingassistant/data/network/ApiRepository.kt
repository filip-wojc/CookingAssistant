package com.cookingassistant.data.network

import com.cookingassistant.data.DTO.LoginRequest
import com.cookingassistant.data.DTO.LoginResponse
import retrofit2.Response

class ApiRepository(private val api: ApiService) {
    suspend fun logInUser(email: String, password:String): Response<LoginResponse> {
        val request = LoginRequest(email = email, password = password)
        return api.logIn(request)
    }
}