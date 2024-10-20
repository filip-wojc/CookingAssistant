package com.cookingassistant.data.DTO
data class RecipeCreateDTO(
    val name: String?,
    val description: String?,
    val imageData: ByteArray?,
    val serves: Int,
    val difficulty: String?,
    val timeInMinutes: Int?,
    val categoryId: Int,
    val ingredientNames: List<String>?,
    val ingredientQuantities: List<String>?,
    val ingredientUnits: List<String>?,
    val steps: List<String>?,
    val nutrientNames: List<String>?,
    val nutrientQuantities: List<String>?,
    val nutrientUnits: List<String>?
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



