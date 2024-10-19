package com.cookingassistant.services

import com.cookingassistant.data.network.ApiRepository
import retrofit2.Response

class RecipeService(private val apiRepository: ApiRepository) {
    suspend fun getAllNutrientsList(): Response<List<String>> {
        return apiRepository.getAllNutrientsList()
    }

}