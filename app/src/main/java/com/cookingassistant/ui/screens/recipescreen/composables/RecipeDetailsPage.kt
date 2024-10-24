package com.cookingassistant.ui.screens.recipescreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipeDetailsPage(
    calories : Int,
    preparationTime : String,
    cookingTime : String,
    url : String,
    ingredients : List<String>,
    size: Float = 0.9f,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp
) {
    Box(
        Modifier
            .fillMaxHeight(size)
            .fillMaxWidth()
            .padding(10.dp)
        ,
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp)
        ) {
            Text(
                text = "Details",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            HorizontalDivider(Modifier.padding(bottom = 20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.Center,
            ) {
                item {
                    Text(
                        "Calories:",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = modifier,
                        fontSize = fontSize,
                        textAlign = TextAlign.Right,
                    )
                }
                item {
                    Text(
                        calories.toString(),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = modifier,
                        fontSize = fontSize
                    )
                }
                item {
                    Text(
                        "Preparation time:",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = modifier,
                        textAlign = TextAlign.Right,
                        fontSize = fontSize
                    )
                }
                item {
                    Text(
                        preparationTime,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = modifier,
                        fontSize = fontSize
                    )
                }
                item {
                    Text(
                        "Cooking time:",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = modifier,
                        textAlign = TextAlign.Right,
                        fontSize = fontSize
                    )
                }
                item {
                    Text(
                        cookingTime,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = modifier,
                        fontSize = fontSize
                    )
                }
            }
        }
        Column(
            Modifier.align(Alignment.Center)
        ) {
            Text(text="Ingredients", color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth(),
                fontSize = fontSize, textAlign = TextAlign.Center)
            for(ingredient in ingredients) {
                Text(
                    text="◼ " + ingredient,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            }
        }


        Column(
            Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Original recipe from:",color = MaterialTheme.colorScheme.onBackground, fontSize=fontSize)
            SelectionContainer {
                Text(
                    text =buildAnnotatedString {
                        withLink(
                            LinkAnnotation.Url(url, TextLinkStyles(style= SpanStyle(color=MaterialTheme.colorScheme.primary)))
                        ) {
                            append(url)
                        }
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize=fontSize
                )
            }
            Spacer(Modifier.padding(bottom = 20.dp))
        }
    }
}