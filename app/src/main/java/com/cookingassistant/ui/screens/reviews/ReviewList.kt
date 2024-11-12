package com.cookingassistant.ui.screens.reviews

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cookingassistant.data.DTO.ReviewGetDTO

@Composable
fun ReviewList(
    reviewViewModel: ReviewViewModel,
    recipeId: Int
) {
    reviewViewModel.loadReviews(recipeId)
    val reviews by reviewViewModel.reviews.collectAsState()
    Box() {
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            reviews?.let {
                items(it.size) { index ->
                    val review = reviews!![index]
                    Review(review)
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }

}