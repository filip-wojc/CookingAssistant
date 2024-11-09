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

    // ##### RESOURCE CONTROLLER ###############################################
    // #
    // #
    @GET("resources/ingredients")
    suspend fun getAllIngredientsList():Response<List<String?>>

    // TODO: TEST
    @GET("resources/difficulties")
    suspend fun getAllDifficultiesList():Response<List<DifficultiesGetDTO>>

    // TODO: TEST
    @GET("resources/occasions")
    suspend fun getAllOccasionsList():Response<List<OccasionsGetDTO>>

    // TODO: TEST
    @GET("resources/categories")
    suspend fun getAllCategoriesList():Response<List<CategoriesGetDTO>>
    // #
    // #
    // #########################################################################


    // ##### AUTH CONTROLLER ###################################################
    // #
    // #
    @POST("users/login")
    suspend fun logIn(@Body loginRequest: LoginRequest):Response<LoginResponse>

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest):Response<String?>
    // #
    // #
    // #########################################################################


    // ##### USER CONTROLLER ###################################################
    // #
    // #
    @POST("users/favourite/{recipeId}")
    suspend fun addRecipeToFavourites(@Path("recipeId") recipeId:Int):Response<Unit>

    @DELETE("users/favourite-recipes/{recipeId}")
    suspend fun removeRecipeFromFavourites(@Path("recipeId") recipeId:Int):Response<Unit>

    @GET("users/favourite-recipes")
    suspend fun getFavouriteRecipes():Response<RecipePageResponse>

    // TODO: TEST
    @GET("users/favourite-recipes/{recipeId}/is-favourite")
    suspend fun checkIfRecipeInFavourites(@Path("recipeId") recipeId: Int):Response<ResponseBody>

    @GET("users/image")
    suspend fun getUserProfilePicture():Response<ResponseBody>

    @Multipart
    @POST("users/image")
    suspend fun addProfilePicture(@Part image: MultipartBody.Part):Response<Unit>

    // TODO: TEST
    // TODO: ADD TO SERVICE
    @PUT("users/change-password")
    suspend fun changePassword()

    // TODO: TEST
    @DELETE("users/delete/{username}")
    suspend fun deleteAccount(
        @Path("username") username:String,
        @Body password: RequestBody
    ): Response<Unit>
    // #
    // #
    // #########################################################################


    // ##### RECIPE CONTROLLER #################################################
    // #
    // #
    @Multipart
    @POST("recipes")
    suspend fun postRecipe(
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody?,
        @Part("Serves") serves: RequestBody,
        @Part("DifficultyId") difficultyId: RequestBody?,
        @Part("TimeInMinutes") timeInMinutes: RequestBody,
        @Part("CategoryId") categoryId: RequestBody,
        @Part("OccasionId") occasionId:RequestBody,
        @Part("Caloricity") caloricity:RequestBody,
        @Part ingredientNames: List<MultipartBody.Part>,
        @Part ingredientQuantities: List<MultipartBody.Part>,
        @Part ingredientUnits: List<MultipartBody.Part>,
        @Part steps: List<MultipartBody.Part>,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @Multipart
    @PUT("recipes/{recipeId}")
    suspend fun modifyRecipe(
        @Path("recipeId") recipeId: Int,
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody?,
        @Part("Serves") serves: RequestBody,
        @Part("DifficultyId") difficultyId: RequestBody?,
        @Part("TimeInMinutes") timeInMinutes: RequestBody,
        @Part("CategoryId") categoryId: RequestBody,
        @Part("OccasionId") occasionId:RequestBody,
        @Part("Caloricity") caloricity:RequestBody,
        @Part ingredientNames: List<MultipartBody.Part>,
        @Part ingredientQuantities: List<MultipartBody.Part>,
        @Part ingredientUnits: List<MultipartBody.Part>,
        @Part steps: List<MultipartBody.Part>,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @DELETE("recipes/{recipeId}")
    suspend fun deleteRecipe(@Path("recipeId") recipeId: Int):Response<Unit>

    @GET("recipes/{recipeId}")
    suspend fun getRecipeDetails(@Path("recipeId") recipeId:Int):Response<RecipeGetDTO>

    @GET("recipes")
    suspend fun getAllRecipes():Response<RecipePageResponse>

    @GET("recipes/names")
    suspend fun getAllRecipeNames():Response<List<RecipeNameDTO>>
    
    @GET("recipes/image/{recipeId}")
    suspend fun getRecipeImage(@Path("recipeId") recipeId:Int):Response<ResponseBody>
    // #
    // #
    // #########################################################################


    // ###### REVIEW CONTROLLER ################################################
    // #
    // #
    @POST("reviews/{recipeId}")
    suspend fun postReview(@Path("recipeId") recipeId: Int, @Body reviewPostDTO: ReviewPostDTO):Response<Unit>


    @POST("reviews/{recipeId}/modify")
    suspend fun modifyReview(@Path("recipeId") recipeId: Int, @Body reviewPostDTO: ReviewPostDTO):Response<Unit>

    @DELETE("reviews/{recipeId}/delete")
    suspend fun deleteReview(@Path("recipeId") recipeId: Int) :Response<Unit>

    @GET("reviews/{recipeId}/my-review")
    suspend fun getMyReview(@Path("recipeId") recipeId:Int):Response<ReviewGetDTO>

    @GET("reviews/{recipeId}")
    suspend fun getAllReviews(@Path("recipeId") recipeId: Int):Response<List<ReviewGetDTO>>

    @GET("reviews/{reviewId}/image")
    suspend fun getReviewImage(@Path("reviewId") reviewId: Int):Response<ResponseBody>
    // #
    // #
    // #########################################################################

}