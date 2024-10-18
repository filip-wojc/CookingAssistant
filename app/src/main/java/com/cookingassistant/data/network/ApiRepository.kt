package com.cookingassistant.data.network
import com.cookingassistant.data.DTO.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiRepository{

    @GET("recipes/nutrientsList")
    suspend fun getAllNutrientsList():List<String>

    @POST("users/login")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest):Response<String?>
}