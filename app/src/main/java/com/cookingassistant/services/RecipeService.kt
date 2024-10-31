package com.cookingassistant.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.DTO.RecipePostDTO
import com.cookingassistant.data.DTO.ReviewPostDTO
import com.cookingassistant.data.network.ApiRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.InputStream

class RecipeService(private val apiRepository: ApiRepository) {
    suspend fun getAllNutrientsList(): Response<List<String?>> {
        return apiRepository.getAllNutrientsList()
    }

    suspend fun getAllIngredientsList(): Response<List<String?>> {
        return apiRepository.getAllIngredientsList()
    }

    suspend fun getRecipeDetails(recipeId: Int):Response<RecipeGetDTO>{
        return apiRepository.getRecipeDetails(recipeId)
    }

    suspend fun getRecipeImageBitmap(recipeId: Int): Bitmap?{
        val response = apiRepository.getRecipeImage(recipeId)

        return if (response.isSuccessful && response.body() != null) {
            val inputStream: InputStream = response.body()!!.byteStream()
            BitmapFactory.decodeStream(inputStream)

        } else {
            println("Failed to fetch image: ${response.code()} - ${response.message()}")
            null
        }
    }

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
        val nutrientNames = convertListToMultipart("NutrientNames", recipe.nutrientNames ?: emptyList())
        val nutrientQuantities = convertListToMultipart("NutrientQuantities", recipe.nutrientQuantities ?: emptyList())
        val nutrientUnits = convertListToMultipart("NutrientUnits", recipe.nutrientUnits ?: emptyList())


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
            nutrientNames,
            nutrientQuantities,
            nutrientUnits,
            imagePart
        )
    }

    suspend fun deleteRecipe(recipeId:Int):Response<Unit>{
        val response = apiRepository.deleteRecipe(recipeId)
        return response
    }

    suspend fun addReview(recipeId: Int, review: ReviewPostDTO):Response<Unit>{
        val response = apiRepository.postReview(recipeId, review)
        return response
    }

    suspend fun addRecipeToFavorites(recipeId:Int):Response<Unit>{
        val response = apiRepository.addRecipeToFavourites(recipeId)
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