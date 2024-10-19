package com.cookingassistant.data.DTO

//IMPORTANT:
//This class is used ONLY to display found recipes in column!
//RecipeItem class is used for containing all informations
//and showing instructions themselves

data class RecipeItemPreview (
    val id: UInt,
    val title: String,
    val difficulty : String,
    val rating : Int,
    val imageUrl : String = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEg8doqTQlvT7J4pArJbagdyOXmZkJ9lvpI81CfQnW_tPoDDeP4a_GSi25tCuMLfmK3Fx9gLjUg6ZORS_yWAVOAEkGcDqx83aI_JikdzwlZO3qG7MMpofUGGVeocBV5OSDWAXY1m78nyBVQC/w640-h426/IMG_5028-2.jpg"
)