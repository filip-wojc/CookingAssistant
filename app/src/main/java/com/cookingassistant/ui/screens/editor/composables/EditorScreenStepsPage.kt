package com.cookingassistant.ui.screens.editor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import com.cookingassistant.ui.screens.editor.EditorScreenViewModel
import com.cookingassistant.ui.screens.editor.Ingredient

@Composable
fun StepsPage(navController: NavHostController, viewModel: EditorScreenViewModel) {

        Column(
            modifier = Modifier.fillMaxSize().
            padding(top = 110.dp, start = 10.dp, end = 10.dp, bottom = 50.dp),
        ){
            StepsMaker(stepList = viewModel.steps,
                onStepListChange = {updatedList ->
                    viewModel.steps = updatedList
                }
            )
        }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (left) = createRefs()


        IconButton(
            onClick = { navController.navigate("details") },
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
    }
}

@Composable
fun StepsMaker(stepList: List<String>,
               onStepListChange: (List<String>) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Button(
        onClick = { editingIndex = null
            showDialog = true },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E72E1)),
        shape = RoundedCornerShape(20)
    ) {
        Text("Add Step")
    }

    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))) {
        items(stepList.size) { index ->
            StepItem (
                upEnabled = index > 0,
                downEnabled = index < stepList.size - 1,
                text = stepList[index],
                onEdit = {
                    editingIndex = index
                    showDialog = true
                },
                onDelete = {
                    val updatedList = stepList.toMutableList().apply { removeAt(index) }
                    onStepListChange(updatedList)
                },
                onMoveUp = {
                    if (index > 0) {
                        val updatedList = stepList.toMutableList().apply {
                            swap(index, index - 1)
                        }
                        onStepListChange(updatedList)
                    }
                },
                onMoveDown = {
                    if (index < stepList.size - 1) {
                        val updatedList  = stepList.toMutableList().apply {
                            swap(index, index + 1)
                        }
                        onStepListChange(updatedList)
                    }
                }
            )
        }
    }

    if (showDialog) {
        StepDialog (
            step = editingIndex?.let { stepList[it] },
            onDismiss = { showDialog = false },
            onSave = { step ->
                val updatedList = stepList.toMutableList().apply {
                    if (editingIndex == null) {
                        add(step)
                    } else {
                        set(editingIndex!!, step)
                    }
                }
                onStepListChange(updatedList)
                showDialog = false
                editingIndex = null
            }
        )
    }

}

@Composable
fun StepItem(
    upEnabled: Boolean,
    downEnabled: Boolean,
    text: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = onEdit)
        .fillMaxWidth()
        .padding(8.dp).border(2.dp, Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text, modifier = Modifier.weight(1f).padding(8.dp))
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
fun StepDialog(
    step: String? = null,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(step ?: "") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add/Edit Step")

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Edit Item") },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (text.isNotEmpty()) {
                            onSave(text)
                        }
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
