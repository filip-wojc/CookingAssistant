package com.cookingassistant.services
import com.cookingassistant.data.DTO.RecipeQuery
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.cookingassistant.data.DTO.CategoriesGetDTO
import com.cookingassistant.data.DTO.DifficultiesGetDTO
import com.cookingassistant.util.RequestBodyFormatter
import com.cookingassistant.data.DTO.OccasionsGetDTO
import com.cookingassistant.util.ApiResponseParser
import com.cookingassistant.data.DTO.RecipeGetDTO
import com.cookingassistant.data.DTO.RecipeNameDTO
import com.cookingassistant.data.DTO.RecipePageResponse
import com.cookingassistant.data.DTO.RecipePostDTO
import com.cookingassistant.data.repositories.ApiRepository
import com.cookingassistant.data.Models.Result
import com.cookingassistant.util.ImageConverter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.InputStream

class RecipeService(private val _apiRepository: ApiRepository) {

    private val _apiResponseParser = ApiResponseParser()
    private val _imageConverter = ImageConverter()
    private val _formatter = RequestBodyFormatter()
    // TODO : TEST
    // TODO : ADD EXCEPTION HANDLING
    suspend fun getAllIngredientsList(): Result<List<String>?> {
        val response =  _apiRepository.getAllIngredientsList()
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    suspend fun getAllUnitsList(): Result<List<String>?> {
        val response =  _apiRepository.getAllUnitList()
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

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getRecipeImageBitmap(recipeId: Int): Result<Bitmap?>{
        val response = _apiRepository.getRecipeImage(recipeId)

        if(response.isSuccessful && response.body() != null) {
            val imageByteArray = response.body()!!.bytes()
            val bitmap = _imageConverter.convertByteArrayToBitmap(imageByteArray)

            return Result.Success(bitmap)
        }
        else{
            // TODO: CHANGE LATER
            return Result.Error("Failed to fetch user image:${response.message()}", errorCode = response.code())
        }
    }

    // TODO: ADD EXCEPTION HANDLING
    suspend fun addRecipe(recipe: RecipePostDTO): Result<Unit?> {
        // Use RequestBodyFormatter for each part
        val namePart = _formatter.fromString(recipe.name)
        val descriptionPart = _formatter.fromString(recipe.description)
        val servesPart = _formatter.fromInt(recipe.serves)
        val difficultyIdPart = _formatter.fromInt(recipe.difficultyId)
        val timeInMinutesPart = _formatter.fromInt(recipe.timeInMinutes)
        val categoryIdPart = _formatter.fromInt(recipe.categoryId)
        val occasionIdPart = _formatter.fromInt(recipe.occasionId)
        val caloricityPart = _formatter.fromInt(recipe.caloricity)

        // Convert lists using helper
        val ingredientNames = _formatter.fromList("IngredientNames", recipe.ingredientNames)
        val ingredientQuantities = _formatter.fromList("IngredientQuantities", recipe.ingredientQuantities)
        val ingredientUnits = _formatter.fromList("IngredientUnits", recipe.ingredientUnits)
        val steps = _formatter.fromList("Steps", recipe.steps)

        // Convert image data if available
        val imagePart = recipe.imageData.let {
            _formatter.fromByteArray(it, "recipe_image.jpg")
        } ?: MultipartBody.Part.createFormData("ImageData", "", "".toRequestBody("image/jpeg".toMediaTypeOrNull()))

        // Prepare all parts for the API call
        val response = _apiRepository.postRecipe(
            namePart, descriptionPart, servesPart, difficultyIdPart, timeInMinutesPart,
            categoryIdPart, occasionIdPart, caloricityPart,
            ingredientNames, ingredientQuantities, ingredientUnits, steps,
            imagePart
        )

        // Handle the API response
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun modifyRecipe(recipeId: Int, recipe: RecipePostDTO):Result<Unit?>{
        // Use RequestBodyFormatter for each part
        val namePart = _formatter.fromString(recipe.name)
        val descriptionPart = _formatter.fromString(recipe.description)
        val servesPart = _formatter.fromInt(recipe.serves)
        val difficultyIdPart = _formatter.fromInt(recipe.difficultyId)
        val timeInMinutesPart = _formatter.fromInt(recipe.timeInMinutes)
        val categoryIdPart = _formatter.fromInt(recipe.categoryId)
        val occasionIdPart = _formatter.fromInt(recipe.occasionId)
        val caloricityPart = _formatter.fromInt(recipe.caloricity)

        // Convert lists using helper
        val ingredientNames = _formatter.fromList("IngredientNames", recipe.ingredientNames)
        val ingredientQuantities = _formatter.fromList("IngredientQuantities", recipe.ingredientQuantities)
        val ingredientUnits = _formatter.fromList("IngredientUnits", recipe.ingredientUnits)
        val steps = _formatter.fromList("Steps", recipe.steps)

        // Convert image data if available
        val imagePart = recipe.imageData.let {
            _formatter.fromByteArray(it, "recipe_image.jpg")
        } ?: MultipartBody.Part.createFormData("ImageData", "", "".toRequestBody("image/jpeg".toMediaTypeOrNull()))

        val response = _apiRepository.modifyRecipe(
            recipeId,
            namePart, descriptionPart, servesPart, difficultyIdPart, timeInMinutesPart,
            categoryIdPart, occasionIdPart, caloricityPart,
            ingredientNames, ingredientQuantities, ingredientUnits, steps,
            imagePart
        )

        val result = _apiResponseParser.wrapResponse(response)

        return result
    }
    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun deleteRecipe(recipeId:Int):Result<Unit?>{
        val response = _apiRepository.deleteRecipe(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }





}