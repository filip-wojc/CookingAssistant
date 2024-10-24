package com.cookingassistant.ui.screens.recipescreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun RecipeScreenFrontPage(
    name : String,
    imageUrl : String,
    desc : String,
    author : String,
    category : String,
    type : String,
    difficulty : String,
    size: Float = 0.9f
) {
    Box(
        Modifier
        .fillMaxHeight(size)
        .fillMaxWidth()
        .padding(10.dp)
        ,
    ) {
        Column (
            Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text=name,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.padding(vertical = 20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.Center
            ) {
                item {
                    Text(text="Author: ${author}", color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(20.dp).fillMaxWidth())
                }
                item {
                    Text(text="Difficulty: ${difficulty}", color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(20.dp).fillMaxWidth())
                }
                item{
                    HorizontalDivider(thickness = 5.dp, modifier = Modifier.padding(vertical = 10.dp))
                }
                item{
                    HorizontalDivider(thickness = 5.dp, modifier = Modifier.padding(vertical = 10.dp))
                }
                item {
                    Text(text="Category: ${category}", color = MaterialTheme.colorScheme.onBackground)
                }
                item {
                    Text(text="Type: ${type}", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        AsyncImage(
            modifier = Modifier.fillMaxHeight()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(50))
            ,
            model = imageUrl,
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )

        Column(
            Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(5.dp)
            ) {
                Text(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Justify,
                    text = desc,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}