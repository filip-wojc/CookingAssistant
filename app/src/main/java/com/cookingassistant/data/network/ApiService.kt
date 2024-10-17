package com.cookingassistant.data.network
import com.cookingassistant.data.DTO.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService{

    @GET("recipes/nutrientsList")
    suspend fun getAllNutrients():List<String>

    @POST("users/login")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LoginResponse>
}