package com.cookingassistant.services
import com.cookingassistant.data.DTO.RecipeQuery
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.CategoriesGetDTO
import com.cookingassistant.data.DTO.DifficultiesGetDTO
import com.cookingassistant.data.DTO.OccasionsGetDTO
import com.cookingassistant.util.ApiResponseParser
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.DTO.RecipeNameDTO
import com.cookingassistant.data.DTO.RecipePageResponse
import com.cookingassistant.data.repositories.ApiRepository
import com.cookingassistant.data.Models.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.InputStream

class RecipeService(private val _apiRepository: ApiRepository) {

    private val _apiResponseParser = ApiResponseParser()

    // TODO : TEST
    // TODO : ADD EXCEPTION HANDLING
    suspend fun getAllIngredientsList(): Result<List<String>?> {
        val response =  _apiRepository.getAllIngredientsList()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getAllOccasionsList(): Result<List<OccasionsGetDTO>?>{
        val response = _apiRepository.getAllOccasionsList()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getAllDifficultiesList(): Result<List<DifficultiesGetDTO>?>{
        val response = _apiRepository.getAllDifficultiesList()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getAllCategoriesList(): Result<List<CategoriesGetDTO>?> {
        val response = _apiRepository.getAllCategoriesList()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun findAllMatchingRecipes(rq: RecipeQuery): Result<RecipePageResponse?>{
        val response = _apiRepository.getAllRecipes(
            SearchPhrase = rq.SearchPhrase,
            IngredientsSearch = rq.Ingredients,
            SortBy = rq.SortBy,
            SortDirection = rq.SortDirection,
            FilterByDifficulty = rq.Difficulty,
            FilterByCategoryName = rq.Category,
            FilterByOccasion = rq.Occasion,
            PageNumber = rq.PageNumber,
            PageSize = rq.PageSize
        )

        val result =  _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getRecipeDetails(recipeId: Int):Result<RecipeGetDTO?>{
        val response = _apiRepository.getRecipeDetails(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getRecipeNames(): Result<List<RecipeNameDTO>?> {
        val response =  _apiRepository.getAllRecipeNames()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    suspend fun getRecipeImageBitmap(recipeId: Int): Bitmap?{
        val response = _apiRepository.getRecipeImage(recipeId)

        return if (response.isSuccessful && response.body() != null) {
            val inputStream: InputStream = response.body()!!.byteStream()
            BitmapFactory.decodeStream(inputStream)

        } else {
            println("Failed to fetch image: ${response.code()} - ${response.message()}")
            null
        }
    }
/*
    suspend fun addRecipe(recipe: RecipePostDTO, imagePath: String): Response<Unit> {
        // Prepare fields as RequestBody
        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), recipe.name ?: "")
        val descriptionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), recipe.description ?: "")
        val servesPart = RequestBody.create("text/plain".toMediaTypeOrNull(), recipe.serves.toString())
        val difficultyPart = RequestBody.create("text/plain".toMediaTypeOrNull(), recipe.difficulty ?: "")
        val timeInMinutesPart = RequestBody.create("text/plain".toMediaTypeOrNull(), recipe.timeInMinutes?.toString() ?: "")
        val categoryIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), recipe.categoryId.toString())

        // Prepare the list fields and convert them to List<MultipartBody.Part>
        // Convert lists to multipart with appropriate form key

        val ingredientNames = convertListToMultipart("IngredientNames", recipe.ingredientNames ?: emptyList())
        val ingredientQuantities = convertListToMultipart("IngredientQuantities", recipe.ingredientQuantities ?: emptyList())
        val ingredientUnits = convertListToMultipart("IngredientUnits", recipe.ingredientUnits ?: emptyList())
        val steps = convertListToMultipart("Steps", recipe.steps ?: emptyList())


        // Prepare image part
        val imagePart = prepareFilePart("ImageData", imagePath)

        // Call the API
        return apiRepository.postRecipe(
            namePart,
            descriptionPart,
            servesPart,
            difficultyPart,
            timeInMinutesPart,
            categoryIdPart,
            ingredientNames,
            ingredientQuantities,
            ingredientUnits,
            steps,
            imagePart
        )
    }
*/
    suspend fun deleteRecipe(recipeId:Int):Response<Unit>{
        val response = _apiRepository.deleteRecipe(recipeId)
        return response
    }



    suspend fun addRecipeToFavorites(recipeId:Int):Response<Unit>{
        val response = _apiRepository.addRecipeToFavourites(recipeId)
        return response
    }

    // Helper function to create MultipartBody.Part from String
    private fun createPartFromString(value: String?): MultipartBody.Part {
        return MultipartBody.Part.createFormData("data", null, RequestBody.create("text/plain".toMediaTypeOrNull(), value ?: ""))
    }

    // Convert List<RequestBody> to List<MultipartBody.Part>
    private fun convertListToMultipart(key: String, parts: List<String?>): List<MultipartBody.Part> {
        return parts.mapIndexed { index, value ->
            MultipartBody.Part.createFormData(key, null, RequestBody.create("text/plain".toMediaTypeOrNull(), value ?: ""))
        }
    }

    // Helper function to prepare the image file as MultipartBody.Part
    private fun prepareFilePart(partName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}