package com.cookingassistant.ui.screens.editor.composables

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.cookingassistant.ui.screens.editor.EditorScreenViewModel
import com.cookingassistant.ui.screens.editor.Ingredient
import androidx.compose.foundation.layout.Row as Row1
import androidx.compose.material3.Button as Button1
import androidx.compose.material3.Text as Text1

@Composable
fun DetailsPage(navController: NavHostController, viewModel: EditorScreenViewModel) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 110.dp, start = 10.dp, end = 10.dp, bottom = 50.dp),
        ) {
            Text1(
                text = "Calories",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            NumberPicker(viewModel.calories){
                selectedCalories ->
                viewModel.calories = selectedCalories
            }
            Text1(
                text = "Preparation time",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            TimePicker(
                timeInMinutes = viewModel.time,
                onTimeSelected = { selectedMinutes -> viewModel.time = selectedMinutes } // zapis do ViewModel
            )
            Text1(
                text = "Serves",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            NumberPicker(viewModel.serves){
                selectedServes ->
                viewModel.serves = selectedServes
            }
            Text1(
                text = "Ingredients",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            IngredientPicker(
                ingredientList = viewModel.igredients,
                onIngredientListChange = { updatedList ->
                    viewModel.igredients = updatedList
                }
            )
        }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (left,right) = createRefs()

        IconButton(
            onClick = { navController.navigate("front") },
            modifier = Modifier.size(56.dp).background(Color(0xFF3700B3), shape = CircleShape)
                .padding(8.dp).constrainAs(left) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Przycisk strzałki",
                tint = Color.White
            )
        }

        IconButton(
            onClick = { navController.navigate("steps") },
            modifier = Modifier.size(56.dp).background(Color(0xFF3700B3), shape = CircleShape)
                .padding(8.dp).constrainAs(right) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Przycisk strzałki",
                tint = Color.White
            )
        }
    }

}

@Composable
fun NumberPicker(number : Int?, onSelectOption: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Button1(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E72E1)),
        shape = RoundedCornerShape(20)
    ) {
        Text1(text = number?.toString() ?: "Select number")
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = Modifier.padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                var currentNumber by remember { mutableStateOf(number?.toString() ?: "0") }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text1(text = "Select Number", style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row1(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            currentNumber = (currentNumber.toIntOrNull()?.minus(1) ?: 0).toString()
                        }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        TextField(
                            value = currentNumber,
                            onValueChange = { newText ->
                                currentNumber = newText.filter { it.isDigit() }
                            },
                            modifier = Modifier.width(80.dp),
                            singleLine = true
                        )

                        IconButton(onClick = {
                            currentNumber = (currentNumber.toIntOrNull()?.plus(1) ?: 0).toString()
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row1(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { showDialog = false }) {
                            Text1("Cancel")
                        }
                        Button1(onClick = {
                            val selectedNumber = currentNumber.toIntOrNull() ?: 0
                            onSelectOption(selectedNumber)
                            showDialog = false
                        }) {
                            Text1("Ok")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimePicker(
    timeInMinutes: Int?,
    onTimeSelected: (Int) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(timeInMinutes?.let { formatTime(it / 60, it % 60) } ?: "Select time") }
    var errorText by remember { mutableStateOf("") }

    Button1(
        onClick = { isDialogOpen = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E72E1)),
        shape = RoundedCornerShape(20)
    ) {
        Text1(text = selectedTime)
    }


    if (isDialogOpen) {
        Dialog(onDismissRequest = { isDialogOpen = false }) {
            var firstHour by remember { mutableStateOf((timeInMinutes ?: 0) / 60) }
            var firstMinute by remember { mutableStateOf((timeInMinutes ?: 0) % 60) }
            Surface {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text1(text = "Select time", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow {
                        item {
                            Column {
                                Text1(
                                    text = "Hour",
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                AndroidView(
                                    factory = { context ->
                                        NumberPicker(context).apply {
                                            minValue = 0
                                            maxValue = 99
                                            value = firstHour
                                            setOnValueChangedListener { _, _, newVal ->
                                                firstHour = newVal
                                            }
                                        }
                                    },
                                    update = { it.value = firstHour }
                                )
                            }
                        }
                        item {
                            Column {
                                Text1(
                                    text = "Minute",
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                AndroidView(
                                    factory = { context ->
                                        NumberPicker(context).apply {
                                            minValue = 0
                                            maxValue = 59
                                            value = firstMinute
                                            setOnValueChangedListener { _, _, newVal ->
                                                firstMinute = newVal
                                            }
                                        }
                                    },
                                    update = { it.value = firstMinute }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text1(text = errorText)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row1 {
                        Button1(onClick = { isDialogOpen = false }) {
                            Text1("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button1(onClick = {
                            if (firstHour == 0 && firstMinute == 0) {
                                errorText = "Need to set first time!"
                            } else {
                                val totalMinutes = firstHour * 60 + firstMinute
                                selectedTime = formatTime(firstHour, firstMinute)
                                onTimeSelected(totalMinutes)
                                isDialogOpen = false
                            }

                        }) {
                            Text1("OK")
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(hour: Int, minute: Int): String {
    val hourText = if (hour > 0) "$hour hours" else ""
    val minuteText = if (minute > 0) "$minute minutes" else ""

    return when {
        hour > 0 && minute > 0 -> "$hourText and $minuteText"
        hour > 0 -> hourText
        minute > 0 -> minuteText
        else -> ""
    }
}

val ingredientOptions = listOf("Flour", "Sugar", "Salt", "Butter")
val unitOptions = listOf("kg", "g", "tsp", "cup")

@Composable
fun IngredientPicker(ingredientList: List<Ingredient>,
                     onIngredientListChange: (List<Ingredient>) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Button(
        onClick = { editingIndex = null
            showDialog = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E72E1)),
        shape = RoundedCornerShape(20)
    ) {
        Text("Add Ingredient")
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))) {
        items(ingredientList.size) { index ->
            IngredientItem(
                upEnabled = index > 0,
                downEnabled = index < ingredientList.size - 1,
                ingredient = ingredientList[index],
                onEdit = {
                    editingIndex = index
                    showDialog = true
                },
                onDelete = {
                    val updatedList = ingredientList.toMutableList().apply { removeAt(index) }
                    onIngredientListChange(updatedList)
                },
                onMoveUp = {
                    if (index > 0) {
                        val updatedList = ingredientList.toMutableList().apply {
                            swap(index, index - 1)
                        }
                        onIngredientListChange(updatedList)
                    }
                },
                onMoveDown = {
                    if (index < ingredientList.size - 1) {
                        val updatedList = ingredientList.toMutableList().apply {
                            swap(index, index + 1)
                        }
                        onIngredientListChange(updatedList)
                    }
                }
            )
        }
    }

    if (showDialog) {
        IngredientDialog(
            ingredient = editingIndex?.let { ingredientList[it] },
            onDismiss = { showDialog = false },
            onSave = { ingredient ->
                val updatedList = ingredientList.toMutableList().apply {
                    if (editingIndex == null) {
                        add(ingredient)
                    } else {
                        set(editingIndex!!, ingredient)
                    }
                }
                onIngredientListChange(updatedList)
                showDialog = false
                editingIndex = null
            }
        )
    }

}

@Composable
fun IngredientItem(
    upEnabled: Boolean,
    downEnabled: Boolean,
    ingredient: Ingredient,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = onEdit)
        .fillMaxWidth()
        .padding(8.dp).border(2.dp, Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically) {
        Text("${ingredient.amount} ${ingredient.unit} ${ingredient.name}", modifier = Modifier.weight(1f).padding(8.dp))
        IconButton(onClick = onMoveUp, enabled = upEnabled) {
            Icon(
                Icons.Default.ArrowUpward,
                contentDescription = "Move Up"
            )

        }
        IconButton(onClick = onMoveDown, enabled = downEnabled) {
            Icon(
                Icons.Default.ArrowDownward,
                contentDescription = "Move Down"
            )
        }
        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
    }
}

@Composable
fun IngredientDialog(
    ingredient: Ingredient? = null,
    onDismiss: () -> Unit,
    onSave: (Ingredient) -> Unit
) {
    var name by remember { mutableStateOf(ingredient?.name ?: "") }
    var amount by remember { mutableStateOf(ingredient?.amount?.toString() ?: "") }
    var unit by remember { mutableStateOf(ingredient?.unit ?: "") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add/Edit Ingredient")

                ComboBox(
                    label = "Ingredient",
                    options = ingredientOptions,
                    selectedOption = name,
                    onOptionSelected = { name = it }
                )

                // Amount Input
                DecimalNumericTextField(
                    label = "Amount",
                    value = amount,
                    onValueChange = { amount = it }
                )

                // Unit Selector
                ComboBox(
                    label = "Unit",
                    options = unitOptions,
                    selectedOption = unit,
                    onOptionSelected = { unit = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (name.isNotBlank() && amount.isNotEmpty() && unit.isNotBlank()) {
                            onSave(Ingredient(name, amount.toDouble(), unit))
                        }
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun DecimalNumericTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            if (input.isEmpty() || input.matches(Regex("^\\d{1,5}(\\.\\d)?\\.?\$"))) {
                onValueChange(input)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Klawiatura numeryczna z kropką
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ComboBox(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E72E1)),
            shape = RoundedCornerShape(20)
        ) {
            Text(text = if (selectedOption.isEmpty()) label else selectedOption)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search $label") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier
                    .height(200.dp)
                    .verticalScroll(scrollState)
                    .padding(8.dp)
            ) {
                options.filter { it.contains(searchQuery, ignoreCase = true) }.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                            searchQuery = ""
                        }
                    )
                }
            }
        }
    }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}