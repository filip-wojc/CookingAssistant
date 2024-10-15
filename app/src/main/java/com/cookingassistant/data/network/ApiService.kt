package com.cookingassistant.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val userId: Int)


interface ApiService{

    @GET("recipes/nutrientsList")
    suspend fun getAllNutrients():List<String>
}