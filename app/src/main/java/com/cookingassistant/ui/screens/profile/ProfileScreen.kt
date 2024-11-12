package com.cookingassistant.ui.screens.profile

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.cookingassistant.R
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import coil3.compose.rememberAsyncImagePainter

@Preview
@Composable
fun ProfileScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profilImage()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Jan Kowalski", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "test@gmail.com", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                profilButton(
                    onClick = {},
                    text = "Favorite recipes",
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    color = Color(0xFF5F75E1)
                )
                profilButton(
                    onClick = {},
                    text = "Own recipes",
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    color = Color(0xFF5F75E1)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            profilButton(onClick = {}, text = "Change Password", color = Color(0xFF9E72E1))
            Spacer(modifier = Modifier.height(8.dp))
            profilButton(onClick = {}, text = "Change E-mail", color = Color(0xFF9E72E1))
            Spacer(modifier = Modifier.height(8.dp))
            profilButton(onClick = {}, text = "Delete Account", color = Color(0xFFEC544C))
            Spacer(modifier = Modifier.height(8.dp))
            profilButton(onClick = {}, text = "Log out", color = Color(0xFF5F75E1))
        }
    }
}


@Composable
private fun profilButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
    color: Color = Color(0xFFFFFFFF),
    text: String = "",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ), shape = RoundedCornerShape(20)
    ) {
        Text(text = text, color = Color(0xFFFFFFFF), fontSize = 20.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun profilImage() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    //Launcher do otwierania galerii
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    ConstraintLayout {
        val (background, profile, editButton) = createRefs()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(0xFF6200EE),
                        1f to Color(0xFF9E72E1)
                    ),
                )
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Image(
            painter = if (selectedImageUri != null) {
                rememberAsyncImagePainter(selectedImageUri)
            } else {
                painterResource(id = R.drawable.projectlogotransparencycircular)
            },
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .border(3.dp, Color.Gray, CircleShape)
                .constrainAs(profile) {
                    top.linkTo(background.bottom)
                    bottom.linkTo(background.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        IconButton(
            onClick = { launcher.launch("image/*") }, //Zmiana zdjęcia
            modifier = Modifier
                .constrainAs(editButton) {
                    top.linkTo(profile.bottom)
                    bottom.linkTo(profile.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .offset(x = 60.dp, y = (-20).dp)
                .border(2.dp, Color.Gray, CircleShape)
                .clip(
                    CircleShape
                )
                .background(color = Color.White),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Profile"
            )
        }
    }
}