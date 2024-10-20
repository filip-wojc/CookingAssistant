package com.cookingassistant.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.network.ApiRepository
import retrofit2.Response
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
}