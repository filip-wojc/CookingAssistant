package com.cookingassistant.data.network
import com.cookingassistant.data.DTO.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiRepository{

    // USER FUNCTIONS
    @POST("users/login")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest):Response<String?>

    // UNIQUE VALUE GETTERS
    @GET("recipes/nutrientsList")
    suspend fun getAllNutrientsList():Response<List<String?>>

    @GET("recipes/ingredientsList")
    suspend fun getAllIngredientsList():Response<List<String?>>

    // GETTERS BY ID
    @GET("recipes/{recipeId}")
    suspend fun getRecipeDetails(@Path("recipeId") recipeId:Int):Response<RecipeGetDTO>

    @GET("recipes/image/{recipeId}")
    suspend fun getRecipeImage(@Path("recipeId") recipeId:Int):Response<ResponseBody>
}