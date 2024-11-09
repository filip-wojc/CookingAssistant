package com.cookingassistant.ui.screens.recipescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cookingassistant.R
import com.cookingassistant.ui.screens.recipescreen.RecipeScreenViewModel

@Composable
fun RecipeRatingPage(
    recipeScreenViewModel: RecipeScreenViewModel,
    size : Float = 0.9f
) {
    val ratingSelection by recipeScreenViewModel.userRating.collectAsState()
    val userComment by recipeScreenViewModel.userComment.collectAsState()
    val maxCommentLength = 150

    Box(
        Modifier
            .fillMaxHeight(size)
            .fillMaxWidth()
    ) {
        Column (
            Modifier
                .align(Alignment.Center)
                .padding(top = 40.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Rate the recipe:", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp)
            Row(modifier = Modifier.padding(bottom = 10.dp))
            {
                for (i in 1..ratingSelection) {
                    IconButton(
                        onClick = {if(ratingSelection != i) {recipeScreenViewModel.onUserRatingChanged(i)} else{recipeScreenViewModel.onUserRatingChanged(0)} },
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.5f))

                    ) {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = "Rating star", tint = Color.Yellow,
                        )
                    }
                }
                for(i in ratingSelection+1..5) {
                    IconButton(
                        onClick = {recipeScreenViewModel.onUserRatingChanged(i)},
                    ) {
                        Icon(imageVector = Icons.Outlined.StarRate, contentDescription = "Rating star", tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            TextField(
                value = userComment,
                onValueChange = {if (it.length <= maxCommentLength) recipeScreenViewModel.onUserCommentChanged(it)},
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text("Additional comment") },
                supportingText = {
                    Text(
                        text = "${userComment.length} / ${maxCommentLength}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            )
            Button(onClick = {recipeScreenViewModel.onRatingSubmited(ratingSelection, userComment)}, enabled = ratingSelection != 0, modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(0.8f)
            ) {
                Text("Submit rating", fontSize = 20.sp)
            }
        }
    }
}