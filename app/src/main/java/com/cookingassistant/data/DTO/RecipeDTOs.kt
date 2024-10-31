package com.cookingassistant.data.DTO

import android.graphics.Bitmap
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class Recipe(
    val name: String?,
    val description: String?,
    val image: Bitmap?,
    val serves: Int,
    val difficulty: String?,
    val timeInMinutes: Int?,
    val categoryId: Int,
    val ingredientNames: List<String?>,
    val ingredientQuantities: List<String?>,
    val ingredientUnits: List<String?>,
    val steps: List<String?>,
    val nutrientNames: List<String?>,
    val nutrientQuantities: List<String?>,
    val nutrientUnits: List<String?>
)

data class RecipeSimpleDTO(
    val id: Int,
    val name: String,
    val description: String,
    val ratings: Float,
    val timeInMinutes: Int,
    val difficulty: String,
    val voteCount: Int,
    val categoryName: String
)

data class RecipePageResponse(
    val items: List<RecipeSimpleDTO>,
    val totalPages: Int,
    val itemsFrom: Int,
    val itemsTo: Int,
    val totalItemsCount: Int
)

data class RecipeNameDTO(
    val id: Int,
    val name: String
)

data class RecipePostDTO(
    val name: String,
    val description: String?,
    val imageData: ByteArray?,
    val serves: Int?,
    val difficulty: String?,
    val timeInMinutes: Int?,
    val categoryId: Int,
    val ingredientNames: List<String?>,
    val ingredientQuantities: List<String?>,
    val ingredientUnits: List<String?>,
    val steps: List<String?>,
    val nutrientNames: List<String?>,
    val nutrientQuantities: List<String?>,
    val nutrientUnits: List<String?>
)

data class RecipeGetDTO(
    val id: Int,
    val name: String,
    val description: String,
    val authorName: String,
    val ratings: Float,
    val timeInMinutes: Int,
    val serves: Int,
    val difficulty: String?,
    val voteCount: Int,
    val categoryName: String,
    val ingredients: List<RecipeIngredientGetDTO>?,
    val nutrients: List<RecipeNutrientGetDTO>?,
    val steps: List<StepGetDTO>?
)

data class RecipeIngredientGetDTO(
    val ingredientName:String?,
    val quantity:String?,
    val unit:String?
)

data class RecipeNutrientGetDTO(
    val nutrientName:String?,
    val quantity: String?,
    val unit:String?
)

data class StepGetDTO(
    val stepNumber:Int,
    val description:String?
)

data class RecipeImage(
    val imageData:ByteArray?
)

data class ReviewPostDTO(
    val value: Int,
    val description: String?
)

data class ReviewGetDTO(
    val id: Int,
    val value: Int,
    val description: String?,
    val reviewAuthor: String?,
    val dateCreated: String,
    val dateModified: String?
)



