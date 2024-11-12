package com.cookingassistant.data

import com.cookingassistant.data.DTO.RecipeIngredientGetDTO

object AdditionalFunctions {
    fun fancyTime(uglyTime : Int) : String {
        val minutes = uglyTime % 60
        val hours = (uglyTime - minutes) / 60
        if (hours == 0) {
            return("${minutes}m")
        }
        return("${hours.toInt()}h ${minutes}m")
    }

    fun fancyIngredients(ingredients : List<RecipeIngredientGetDTO>?) : List<String> {
        if (ingredients == null) {
            return listOf()
        }
        var result : MutableList<String> = mutableListOf()
        for (ingredient in ingredients) {
            val name = ingredient.ingredientName ?: ""
            val unit = ingredient.unit ?: ""
            val quantity = ingredient.quantity ?: ""
            if (quantity.contains(' ')) { //external recipe
                if(result.contains(quantity)) {
                    continue
                }
                result.add("${quantity}")
                continue
            }
            result.add("${quantity} ${unit} ${name}")
        }
        return result
    }
}