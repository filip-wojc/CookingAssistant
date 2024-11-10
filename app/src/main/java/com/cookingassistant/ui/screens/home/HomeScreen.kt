package com.cookingassistant.ui.screens.home

import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cookingassistant.data.DTO.RecipePostDTO
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import com.cookingassistant.util.ImageConverter

@Composable
fun HomeScreen(navController: NavController, homeScreenViewModel: HomeScreenViewModel){
    val recipeImage by homeScreenViewModel.recipeImage.collectAsState()
    val userProfileImage by homeScreenViewModel.userProfileImage.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to the Home Screen", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/ja.jpg"
            val imageConverter = ImageConverter()
            val imageByteArray = imageConverter.convertImageToByteArray(imagePath)


                         }, modifier = Modifier.fillMaxWidth()) {
            Text("Show Image")
        }
        if (userProfileImage != null) {
            AlertDialog(
                onDismissRequest = { /* Handle dismiss */ },
                title = { Text(text = "User Image") },
                text = {
                    Image(
                        bitmap = userProfileImage!!.asImageBitmap(),
                        contentDescription = "Recipe Image"
                    )
                },
                confirmButton = {
                    Button(onClick = { /* Handle close */ }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}


