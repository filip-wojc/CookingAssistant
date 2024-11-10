package com.cookingassistant.ui.screens.editor


import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import com.cookingassistant.R
import androidx.compose.runtime.remember as remember1

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String
)

class EditorScreenViewModel() : ViewModel() {
    var name by mutableStateOf<String?>(null)
    var description by mutableStateOf("")
    var image by mutableStateOf<Uri?>(null)
    var serves by mutableStateOf<Int?>(null)
    var difficulty by mutableStateOf<String?>(null)
    var time by mutableStateOf<Int?>(null)
    var category by mutableStateOf<String?>(null)
    var occasion by mutableStateOf<String?>(null)
    var calories by mutableStateOf<Int?>(null)
    var igredients by mutableStateOf(listOf<Ingredient>())
    var steps by mutableStateOf(listOf<String>())
}