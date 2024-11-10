package com.cookingassistant.ui.screens.editor.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.cookingassistant.R
import com.cookingassistant.ui.screens.editor.EditorScreenViewModel

@Composable
fun FrontPage(navController: NavHostController, viewModel: EditorScreenViewModel) {

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            viewModel.image = uri
        }
    }

    LazyColumn (
            modifier = Modifier.fillMaxSize().
            padding(top = 110.dp, start = 10.dp, end = 10.dp, bottom = 50.dp),
        ) {
            item { Text(text = "Name", fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp)) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {TextField(
                value = viewModel.name ?: "",
                label = { Text("Enter your name") },
                onValueChange = {
                    viewModel.name = it
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )}
            item {Spacer(modifier = Modifier.height(8.dp))}
            item{
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Column(modifier =  Modifier.weight(1f)) {
                        Text(text = "Difficulty", fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                        SearchableDropdownButton(
                            defaultText = viewModel.difficulty ?: "Choose Difficulty",
                            options = listOf("Easy", "Medium", "Hard"),
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        ){
                                selectedDifficulty ->
                            viewModel.difficulty = selectedDifficulty
                        }
                    }
                    Column(modifier =  Modifier.weight(1f)) {
                        Text(text = "Occasion", fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                        SearchableDropdownButton(
                            defaultText = viewModel.occasion ?: "Choose Occasion",
                            options = listOf("Breakfast", "Lunch", "Dinner", "Snack"),
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                        ){
                                selectedOccasion ->
                            viewModel.occasion = selectedOccasion
                        }
                    }
                }
            }
            item{Spacer(modifier = Modifier.height(8.dp))}
            item{Column(modifier = Modifier.fillMaxWidth()){
                Text(text = "Category", fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                SearchableDropdownButton(
                    defaultText = viewModel.category ?: "Choose Category",
                    options = listOf("Vegetarian", "Vegan", "Gluten-Free", "Keto"),
                    modifier = Modifier.fillMaxWidth(0.8f).align(Alignment.CenterHorizontally)
                ){
                    selectedCategory ->
                    viewModel.category = selectedCategory
                }
            }}
            item{Spacer(modifier = Modifier.height(8.dp))}
            item{Text(text = "Recipe image", fontSize = 18.sp, modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally))}
            item{Spacer(modifier = Modifier.height(8.dp))}

            item{ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (profile, editButton) = createRefs()

                Image(
                    painter = rememberAsyncImagePainter(viewModel.image ?: R.drawable.ic_launcher_background),
                    contentDescription = "recipePicture",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.constrainAs(profile){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .constrainAs(editButton) {
                            top.linkTo(profile.bottom)
                            bottom.linkTo(profile.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
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
            }}
            item{Spacer(modifier = Modifier.height(8.dp))}
            item{Text(text = "Description", fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))}

            item{Spacer(modifier = Modifier.height(8.dp))}

            item{TextField(
                value = viewModel.description,
                label = { Text("Enter your description") },
                onValueChange = {
                    viewModel.description = it
                },
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )}
        }


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (right) = createRefs()

        IconButton(
            onClick = { navController.navigate("details") },
            modifier = Modifier.size(56.dp).background(Color(0xFF3700B3), shape = CircleShape)
                .padding(8.dp).constrainAs(right) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Przycisk strzałki",
                tint = Color.White // Kolor strzałki
            )
        }
    }
}


@Composable
fun SearchableDropdownButton(
    defaultText: String = "Choose option",
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelectOption: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf(defaultText) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val filteredOptions = options.filter { it.contains(searchText, ignoreCase = true) }

    Button(
        modifier = modifier,
        onClick = { isDialogOpen = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E72E1)),
        shape = RoundedCornerShape(20)
    ) {
        Text(selectedOption)
    }

    if (isDialogOpen) {
        Dialog(onDismissRequest = { isDialogOpen = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text("Search...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        filteredOptions.forEach { option ->
                            TextButton(
                                onClick = {
                                    selectedOption = option
                                    onSelectOption(option)
                                    isDialogOpen = false
                                    searchText = ""
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(option)
                            }
                        }
                    }
                    Button(onClick = { isDialogOpen = false }, modifier =Modifier.align(Alignment.End)) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}