package com.cookingassistant.ui.screens.reviews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cookingassistant.data.DTO.ReviewGetDTO

@Composable
fun ReviewList(
    reviewViewModel: ReviewViewModel,
) {
    val recipeId by reviewViewModel.recipeId.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()
    val userReview by reviewViewModel.currentUserReview.collectAsState()
    reviewViewModel.LoadReviewsImage(reviews)
    val images by reviewViewModel.reviewImages.collectAsState()
    LazyColumn(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(top = 60.dp),
    ) {
        if(reviews == null) {
            return@LazyColumn
        }
        items(reviews!!) { review ->
            Review(review, userReview,images, reviewViewModel, recipeId)
        }
    }
}