package com.cookingassistant.data.DTO

//IMPORTANT:
//RecipeItem class is used for containing ALL informations
//about the recipe and showing instructions themselves

//To be completed
data class RecipeItem(
    val Id : UInt,
    val ImageUrl : String,
    val Name : String,
    val Description : String,
    val Author : String,
    val Category: String,
    val Type: String,

    val Difficulty : String,
    val Rating : Int,
    val Ingredients : List<String>,
    val Calories: Int,
    val PreparationTime: String,
    val CookingTime: String,
    val Url: String,

    val Steps : List<String>,
)
