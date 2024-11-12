package com.cookingassistant.ui.screens.reviews
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cookingassistant.data.DTO.ReviewGetDTO
import java.time.LocalDate
import java.util.Date

@Composable
fun Review(reviewGetDTO: ReviewGetDTO, images: MutableMap<Int, Bitmap?>) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val image = images.getOrDefault(reviewGetDTO.id, null)
                if (image != null) {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "profile-pic",
                        modifier = Modifier.width(48.dp).height(48.dp).clip(CircleShape),
                    )
                }
                else {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        tint = Color.Gray,
                        contentDescription = "profile-pic",
                        modifier = Modifier.width(48.dp).height(48.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(reviewGetDTO.reviewAuthor.toString(), fontSize = 18.sp,color = MaterialTheme.colorScheme.onBackground)
            }

            Row {
                for (i in 1..reviewGetDTO.value) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        tint = Color.Yellow,
                        contentDescription = "star",
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.inverseSurface.copy(0.5f))
                            .size(30.dp)
                            .padding(4.dp)

                    )
                }
                for (j in reviewGetDTO.value + 1..5) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "star",
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.inverseSurface.copy(0.5f))
                            .size(30.dp)
                            .padding(4.dp)

                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .border(2.dp, Color.LightGray, RoundedCornerShape(20.dp))
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = reviewGetDTO.description.toString(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(5.dp))

        Text(
            text = "Reviewed at: ${reviewGetDTO.dateCreated.substring(0,10)}",
            Modifier.padding(start = 8.dp),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
