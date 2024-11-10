package com.cookingassistant.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cookingassistant.ui.screens.FilterScreen.FilterScreenViewModel

@Composable
fun ExtendableList(
    title: String,
    items: List<String>,
    filterScreenViewModel: FilterScreenViewModel,
    filterType : String, //Occasion, Category
    fontColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onBackground,
    limit: Int = 2
) {
    var isExtended by remember{ mutableStateOf(false) }
    var refresh by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp)
        )
        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 5.dp),
        ) {
            if(refresh) {
                for (i in 0..<items.size) {
                    if (i > limit - 1 && !isExtended) { //
                        break
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .padding(end = 10.dp, top = 2.dp, bottom = 2.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .wrapContentSize(Alignment.Center)
                                .clickable {
                                    filterScreenViewModel.onFilterChange(
                                        filterType,
                                        items[i]
                                    )
                                    refresh = false
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = if (items[i] == filterScreenViewModel.getFilterValue(
                                        filterType
                                    )
                                ) Icons.Outlined.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                                contentDescription = null,
                                Modifier.clickable {
                                    filterScreenViewModel.onFilterChange(
                                        filterType,
                                        items[i]
                                    )
                                    refresh = false
                                },
                            )
                            Text(
                                fontSize = 15.sp,
                                text = items[i],
                                color = fontColor,
                                modifier = Modifier.weight(1f).padding(start = 5.dp)
                            )

                        }
                    }
                }
            } else {
                for (i in 0..<items.size) {
                    if (i > limit - 1 && !isExtended) { //
                        break
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .padding(end = 10.dp, top = 2.dp, bottom = 2.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .wrapContentSize(Alignment.Center)
                                .clickable {
                                    filterScreenViewModel.onFilterChange(
                                        filterType,
                                        items[i]
                                    )
                                    refresh = true
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = if (items[i] == filterScreenViewModel.getFilterValue(
                                        filterType
                                    )
                                ) Icons.Outlined.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                                contentDescription = null,
                                Modifier.clickable {
                                    filterScreenViewModel.onFilterChange(
                                        filterType,
                                        items[i]
                                    )
                                    refresh = true
                                },
                            )
                            Text(
                                fontSize = 15.sp,
                                text = items[i],
                                color = fontColor,
                                modifier = Modifier.weight(1f).padding(start = 5.dp)
                            )

                        }
                    }
                }
            }

        }
        ClickableText(
            text = if(!isExtended) AnnotatedString("Show more") else AnnotatedString("Show less"),
            onClick = {
                isExtended = !isExtended
            },
            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
            style = TextStyle.Default.copy(color = Color.Blue)
        )
    }
}