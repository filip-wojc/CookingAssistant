package com.cookingassistant.services

import android.graphics.Bitmap
import com.cookingassistant.data.DTO.ReviewGetDTO
import com.cookingassistant.data.DTO.ReviewPostDTO
import com.cookingassistant.data.Models.Result
import com.cookingassistant.data.repositories.ApiRepository
import com.cookingassistant.util.ApiResponseParser
import com.cookingassistant.util.ImageConverter

class ReviewService(private val _apiRepository: ApiRepository) {

    private val _apiResponseParser = ApiResponseParser()
    private val _imageConverter = ImageConverter()

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun addReview(recipeId: Int, reviewPostDTO: ReviewPostDTO):Result<Unit?>{

        val response = _apiRepository.postReview(recipeId, reviewPostDTO)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun modifyReview(recipeId: Int, reviewPostDTO: ReviewPostDTO):Result<Unit?>{

        val response = _apiRepository.modifyReview(recipeId, reviewPostDTO)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun deleteReview(recipeId: Int):Result<Unit?>{
        val response = _apiRepository.deleteReview(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getMyReview(recipeId: Int):Result<ReviewGetDTO?>{
        val response = _apiRepository.getMyReview(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getAllReviews(recipeId: Int):Result<List<ReviewGetDTO>?> {
        val response = _apiRepository.getAllReviews(recipeId)
        val result = _apiResponseParser.wrapResponse(response)

        return result
    }

    // TODO: TEST
    // TODO: ADD EXCEPTION HANDLING
    suspend fun getReviewImageBitmap(reviewId: Int): Result<Bitmap?>{
        val response = _apiRepository.getReviewImage(reviewId)
        if(response.isSuccessful && response.body() != null){
            val imageByteArray = response.body()!!.bytes()
            val bitmap = _imageConverter.convertByteArrayToBitmap(imageByteArray)

            return Result.Success(bitmap)
        }
        else{
            return Result.Error("Failed to fetch user image:${response.message()}", errorCode = response.code())
        }

    }
}