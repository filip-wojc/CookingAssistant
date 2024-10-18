package com.cookingassistant.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun RatingToStars(rating : Int,
                  modifier : Modifier = Modifier,
                  content: @Composable() () -> Unit
) {
    var i = 0
    Row(
    ) {

        content()
        if(rating == 0 ) {
            Text(" not rated yet.",
                fontSize = 15.sp)
        } else {
            for (i in 0..<rating) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "star",
                    modifier = modifier,
                )
            }
        }
    }
}