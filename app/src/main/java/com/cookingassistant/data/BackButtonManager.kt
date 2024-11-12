package com.cookingassistant.data

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cookingassistant.data.network.ApiRepository
import com.cookingassistant.data.network.RetrofitClient
import com.cookingassistant.services.AuthService
import com.cookingassistant.services.RecipeService
import com.cookingassistant.services.UserService
import com.cookingassistant.ui.composables.topappbar.TopAppBarViewModel
import com.cookingassistant.ui.screens.recipescreen.RecipeScreenViewModel

object BackButtonManager {
    //lateinit var navController:NavController //Memory leak 💀
    var activeTool = ""
    lateinit var topAppBarViewModel: TopAppBarViewModel

    /*
    fun navigate(destination:String) {
        navController.navigate(destination)
        activeTool = ""
    }
    */


}