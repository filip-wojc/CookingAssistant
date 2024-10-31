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
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiRepository{
    // NUTRIENTS CONTROLLER
    // TODO: TEST
    @GET("nutrients")
    suspend fun getAllNutrientsList():Response<List<String?>>
    // INGREDIENTS CONTROLLER
    // TODO : TEST
    @GET("ingredients")
    suspend fun getAllIngredientsList():Response<List<String?>>

    // USER CONTROLLER
    @POST("users/login")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest):Response<String?>

    // TODO: Test
    @POST("users/favourite/{recipeId}")
    suspend fun addRecipeToFavourites(@Path("recipeId") recipeId:Int):Response<Unit>
    // TODO : IMPLEMENT
    //@GET(users/favourite-recipes")
    // suspend fun getFavouriteRecipes
    // TODO : IMPLEMENT
    //@DELETE("users/favourite-recipes/{recipeId}")
    //suspend fun removeRecipeFromFavourites

    @GET("users/image")
    suspend fun getUserProfilePicture():Response<ResponseBody>
    // TODO : IMPLEMENT
    //@POST("users/image")
    // suspend fun addProfilePicture
    // TODO : Test
    @DELETE("users/delete/{username}")
    suspend fun deleteAccount(@Path("username") username:String): Response<Unit>

    // RECIPE CONTROLLER
    // TODO : Test
    @Multipart
    @POST("recipes")
    suspend fun postRecipe(
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody?,
        @Part("Serves") serves: RequestBody,
        @Part("Difficulty") difficulty: RequestBody?,
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

    // TODO : TEST
    @Multipart
    @PUT("recipes/{recipeId}")
    suspend fun modifyRecipe(
        @Path("recipeId") recipeId: Int,
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody?,
        @Part("Serves") serves: RequestBody,
        @Part("Difficulty") difficulty: RequestBody?,
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
    @DELETE("recipes/{recipeId}")
    suspend fun deleteRecipe(@Path("recipeId") recipeId: Int):Response<Unit>

    // TODO : Test
    @GET("recipes/{recipeId}")
    suspend fun getRecipeDetails(@Path("recipeId") recipeId:Int):Response<RecipeGetDTO>

    // TODO : Test
    @GET("recipes")
    suspend fun getAllRecipes():Response<RecipePageResponse>

    // TODO : Test
    @GET("recipes/names")
    suspend fun getAllRecipeNames():Response<List<RecipeNameDTO>>
    
    @GET("recipes/image/{recipeId}")
    suspend fun getRecipeImage(@Path("recipeId") recipeId:Int):Response<ResponseBody>

    // REVIEW CONTROLLER
    // TODO : Test
    @POST("reviews/{recipeId}")
    suspend fun postReview(@Path("recipeId") recipeId: Int, @Body reviewPostDTO: ReviewPostDTO):Response<Unit>

    // TODO : Test
    @POST("reviews/{recipeId}/modify")
    suspend fun modifyReview(@Path("recipeId") recipeId: Int, @Body reviewPostDTO: ReviewPostDTO):Response<Unit>
    // TODO : Test
    @DELETE("reviews/{recipeId}/delete")
    suspend fun deleteReview(@Path("recipeId") recipeId: Int) :Response<Unit>
    // TODO : Test
    @GET("reviews/{recipeId}/my-review")
    suspend fun getMyReview(@Path("recipeId") recipeId:Int):Response<ReviewGetDTO>
    // TODO : Test
    @GET("reviews/{recipeId}")
    suspend fun getAllReviews(@Path("recipeId") recipeId: Int):Response<List<ReviewGetDTO>>
    // TODO : Test
    @GET("reviews/{reviewId}/image")
    suspend fun getReviewImage(@Path("reviewId") reviewId: Int):Response<ResponseBody>


}