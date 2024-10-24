package com.cookingassistant.ui.screens.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cookingassistant.ui.composables.topappbar.TopAppBar

@Preview
@Composable
fun ProfileScreen(){
    TopAppBar {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(250.dp).
                background(color = Color(0xFF5e3bee),
                    shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
                ),
                contentAlignment = Alignment.TopCenter
            ){
                Text(modifier = Modifier.padding(top = 150.dp),
                    text = "Account",
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Username: ", )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "E-mail: ")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {Unit}) {
                Text("Change Password")
            }
        }
    }
}