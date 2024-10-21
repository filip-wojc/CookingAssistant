package com.cookingassistant.data.network
import com.cookingassistant.data.DTO.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiRepository{

    // // // USER SERVICE FUNCTIONS // // //
    @POST("users/login")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest):Response<String?>

    // TODO: Test
    @POST("users/favorite/{recipeId}")
    suspend fun addRecipeToFavorites(@Path("recipeId") recipeId:Int):Response<Unit>

    // // // RECIPE SERVICE FUNCTIONS // // //
    // TODO : Test
    @Multipart
    @POST("recipes")
    suspend fun createRecipe(
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Serves") serves: RequestBody,
        @Part("Difficulty") difficulty: RequestBody,
        @Part("TimeInMinutes") timeInMinutes: RequestBody,
        @Part("CategoryId") categoryId: RequestBody,
        @Part ingredientNames: List<MultipartBody.Part>,
        @Part ingredientQuantities: List<MultipartBody.Part>,
        @Part ingredientUnits: List<MultipartBody.Part>,
        @Part steps: List<MultipartBody.Part>,
        @Part nutrientNames: List<MultipartBody.Part>,
        @Part nutrientQuantities: List<MultipartBody.Part>,
        @Part nutrientUnits: List<MultipartBody.Part>,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    // TODO : Test
    @POST("recipes/{recipeId}/review")
    suspend fun postReview(@Path("recipeId") recipeId: Int, @Body reviewPostDTO: ReviewPostDTO):Response<Unit>

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

    // TODO : GetAllRecipes()

    // TODO : Test
    @GET("recipes")
    suspend fun getAllRecipes():Response<List<RecipeGetDTO>>

    // TODO : Test
    @DELETE("recipes/{recipeId}")
    suspend fun deleteRecipe(@Path("recipeId") recipeId: Int):Response<Unit>
}