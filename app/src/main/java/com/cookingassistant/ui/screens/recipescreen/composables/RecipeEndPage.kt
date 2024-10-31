package com.cookingassistant.ui.screens.recipescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import com.cookingassistant.R
import com.cookingassistant.ui.screens.recipescreen.RecipeScreenViewModel

@Composable
fun RecipeEndPage(
     recipeScreenViewModel: RecipeScreenViewModel,
     size : Float = 0.9f
) {
    val ratingSelection by recipeScreenViewModel.userRating.collectAsState()
    val favorite by recipeScreenViewModel.markedFavorite.collectAsState()
    val userComment by recipeScreenViewModel.userComment.collectAsState()
    val maxCommentLength = 150

    Box(
        Modifier
            .fillMaxHeight(size)
            .fillMaxWidth()
    ) {
       Box(Modifier
           .align(Alignment.TopCenter)
           .fillMaxWidth()
           .fillMaxHeight(0.33f)) {
           Image(
               painter = painterResource(R.drawable.projectlogo2kcircular),
               contentDescription = "Cooking assistant app",
               modifier = Modifier.fillMaxWidth()
                   .background(MaterialTheme.colorScheme.tertiaryContainer)

           )
           Icon(imageVector = Icons.Filled.Check,
               contentDescription = "Bravo",
               modifier = Modifier
                   .fillMaxSize(1f)
               ,
               tint = MaterialTheme.colorScheme.tertiary
           )

           Text(text="\nDONE!",
               color= MaterialTheme.colorScheme.tertiary,
               modifier = Modifier.align(Alignment.BottomCenter),
               textAlign = TextAlign.Center,
               fontSize = 24.sp,
               fontWeight = FontWeight.Bold
           )
       }

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

        Button(onClick = {recipeScreenViewModel.onFavoriteChanged(!favorite)},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .height(50.dp)
        ) {
            if(favorite) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "mark favorite button", tint = Color.Red)
                Text(
                    text = "ADDED TO FAVORITES!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "mark favorite button")
                Text(
                    text = "ADD TO FAVORITES",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            if(favorite) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "mark favorite button", tint = Color.Red)
            } else {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "mark favorite button")
            }
        }
    }
}