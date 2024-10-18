package com.cookingassistant.ui.screens.RecipesList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.cookingassistant.data.RecipeItemPreview
import com.cookingassistant.ui.composables.RatingToStars
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*

//---------------------------------//
//TESTING AREA, TO BE REMOVED LATER//
//---------------------------------//

fun simulateDatabaseQuery() : List<RecipeItemPreview>  {
    val result : List<RecipeItemPreview> =
    runBlocking {
             simulateDatabaseConnection().await()
    }
    return result
}

suspend fun simulateDatabaseConnection(): Deferred<List<RecipeItemPreview>> = coroutineScope {
    async {
        delay(1000)
        val recipes: MutableList<RecipeItemPreview> = mutableListOf()
        recipes.add( RecipeItemPreview(1u,"Chicken Soup", "Easy", 1 ))
        recipes.add( RecipeItemPreview(2u,"Hot Soup", "Easy", 2 ))
        recipes.add( RecipeItemPreview(3u,"Bad Soup", "Easy", 4 ))
        recipes.add( RecipeItemPreview(35u,"Super Soup", "More Effort", 5 ))
        recipes.add( RecipeItemPreview(25u,"Robert Soup", "Easy", 0 ))
        recipes.add( RecipeItemPreview(123u,"Dawid Soup", "Easy", 0 ))
        recipes.add( RecipeItemPreview(6u,"Kotlin Soup", "Easy", 2 ))
        recipes.add( RecipeItemPreview(166u,"Chicken Spicy", "Hard", 4 ))
        recipes.add( RecipeItemPreview(13u,"Chicken NotSpicy", "Hard", 3 ))
        recipes.add( RecipeItemPreview(22u,"Chicken AlmostSpicy", "Easy", 1 ))
        recipes.add( RecipeItemPreview(125u,"Chicken SuperSpicy", "Easy", 5 ))
        recipes.add( RecipeItemPreview(53u,"Chicken", "More Effort", 4 ))
        recipes.add( RecipeItemPreview(67u,"Not Chicken", "Easy", 2 ))
        recipes.add( RecipeItemPreview(67u,"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "Easy", 3 ))
        return@async recipes
    }
}

@Preview
@Composable
fun TestRecipesColumn() {
    val recipesViewModel : RecipesListViewModel = viewModel()
    recipesViewModel.loadRecipes(simulateDatabaseQuery())
    RecipesColumn(recipesViewModel)
}

//---------------------------------//
//----------END OF TESTING---------//
//---------------------------------//

@Composable
fun RecipesColumn(
    viewModel: RecipesListViewModel = viewModel()
) {
    val recipesPreviewList by viewModel.recipes.collectAsState()

    TopAppBar(searchQuery = "Soup") {
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, start = 10.dp, end = 10.dp, bottom = 50.dp)
                .fillMaxWidth()
        ) {
            item {
                Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) }
            items(recipesPreviewList) {item ->
                Column(
                    Modifier.padding(10.dp)
                        .height(400.dp)
                        .clickable {  },
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                            .height(80.dp)
                    ) {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight(500),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
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
                                text = "Difficulty: ${item.difficulty}",
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                fontSize = 15.sp
                            )
                            RatingToStars(item.rating,
                                modifier = Modifier.size(20.dp).shadow(elevation = 4.dp)
                            ) {
                                Text(
                                    text = "Rating:",
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                    AsyncImage(
                        modifier = Modifier.fillMaxHeight().clip(RoundedCornerShape(10.dp)),
                        model = item.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                    )
                }


                HorizontalDivider(Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }

}
