package com.cookingassistant.ui.screens.RecipesList

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cookingassistant.data.objects.TextFormatting
import com.cookingassistant.ui.composables.RatingToStars
import kotlin.math.roundToInt


@Composable
fun RecipeList(
    viewModel: RecipesListViewModel = viewModel()
) {
    val recipesPreviewList by viewModel.recipes.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val inputPageNumber by viewModel.inputPageNumber.collectAsState()
    val images by viewModel.recipeImages.collectAsState()
    val foundResult by viewModel.foundResults.collectAsState()

    val sizeAnim1 by animateFloatAsState(
        targetValue = if (foundResult == 1) 1f else 0f
    )

    if(foundResult == 2) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.error,
                text = "No results found or failed to fetch recipes."
            )
        }
        return
    } else if(foundResult == 0) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = "Loading..."
            )
        }
        return
    }
    Box(
        Modifier.fillMaxWidth().fillMaxHeight(sizeAnim1)
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(bottom = 50.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            item { Spacer(Modifier.fillMaxWidth().padding(bottom = 50.dp)) }
            item {
                Row(modifier = Modifier.fillMaxWidth().height(50.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                    if(currentPage != 1) {
                        IconButton(
                            modifier = Modifier.padding(end = 10.dp),
                            onClick = { viewModel.onPageButtonClicked(-1) }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Filled.KeyboardDoubleArrowLeft,
                                contentDescription = "Page back",
                                tint = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    }
                    if(currentPage < totalPages) {
                        IconButton(
                            onClick = { viewModel.onPageButtonClicked(1) }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                                contentDescription = "Page next",
                                tint = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    }
                    Text(
                        textAlign = TextAlign.End,
                        fontSize = 20.sp,
                        text = "Page $currentPage out of $totalPages",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth().padding(end = 10.dp).weight(1f)
                    )

                }

            }
            item{
                Row(modifier=Modifier.fillMaxWidth().padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputPageNumber,
                        onValueChange = {viewModel.onInputPageNumberChange(it)},
                        singleLine = true,
                        shape = AbsoluteCutCornerShape(0.dp),
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            viewModel.onPageNumberSubmit()
                        }),
                        label = {Text(
                            text="Go to page",
                            modifier = Modifier.padding(end = 10.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )}
                    )
                }

            }
            items(recipesPreviewList) {item ->
                Column(
                    Modifier.padding(10.dp)
                        .height(500.dp)
                        .clickable { viewModel.onRecipeClick(item.id) }
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentHeight(Alignment.CenterVertically)
                        .padding(bottom = 10.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                            .height(100.dp)
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            text = item.name,
                            fontWeight = FontWeight(500),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,

                            )
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.onBackground,
                                text = "Difficulty: ${item.difficultyName}",
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                fontSize = 15.sp
                            )
                            RatingToStars(item.ratings.roundToInt(),
                                modifier = Modifier.size(20.dp).shadow(elevation = 4.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            ) {
                                Text(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    text = "Rating:",
                                    fontSize = 15.sp
                                )
                            }
                            Text(
                                color = MaterialTheme.colorScheme.onBackground,
                                text = "Vote count: ${item.voteCount}",
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                fontSize = 15.sp
                            )
                        }
                    }
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "Category: ${item.categoryName}, Occasion: ${item.occasionName}",
                        fontSize = 15.sp,
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "Calories: ${item.caloricity}, Completion time: ${TextFormatting.formatTime(item.timeInMinutes)}",
                        fontSize = 15.sp
                    )
                    if(images.containsKey(item.id)) {
                        if(images.getValue(item.id) != null) {
                            val rawBitmap : Bitmap = images.getValue(item.id)!!
                            Image(
                                contentScale = ContentScale.Crop,
                                bitmap = rawBitmap.asImageBitmap(),
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).weight(1f).fillMaxWidth().padding(10.dp),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth().height(50.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        text = "Page $currentPage out of $totalPages",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth().padding(end = 10.dp).weight(1f)
                    )

                    if(currentPage != 1) {
                        IconButton(
                            modifier = Modifier.padding(end = 10.dp),
                            onClick = { viewModel.onPageButtonClicked(-1) }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Filled.KeyboardDoubleArrowLeft,
                                contentDescription = "Page back",
                                tint = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    }
                    if(currentPage < totalPages) {
                        IconButton(
                            onClick = { viewModel.onPageButtonClicked(1) }
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                                contentDescription = "Page next",
                                tint = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    }
                }

            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    }
}
