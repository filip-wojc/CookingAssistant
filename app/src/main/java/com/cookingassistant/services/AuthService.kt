package com.cookingassistant.services

import com.cookingassistant.data.DTO.LoginRequest
import com.cookingassistant.data.DTO.LoginResponse
import com.cookingassistant.data.Models.Result
import com.cookingassistant.data.DTO.RegisterRequest
import com.cookingassistant.data.repositories.ApiRepository
import com.cookingassistant.util.ApiResponseParser
import retrofit2.Response

class AuthService(private val _apiRepository: ApiRepository) {
    private val _apiResponseParser = ApiResponseParser()

    // TODO: ADD EXCEPTION HANDLING
    suspend fun logInUser(email: String, password:String): Result<LoginResponse?> {
        val requestBody = LoginRequest(
            email,
            password
        )
        val response = _apiRepository.logIn(requestBody)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: ADD EXCEPTION HANDLING
    suspend fun registerUser(username: String, email: String, password: String): Result<Unit?>
    {
        val requestBody = RegisterRequest(
            username,
            email,
            password
        )
        val response = _apiRepository.register(requestBody)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }





}