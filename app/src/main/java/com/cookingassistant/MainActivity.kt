package com.cookingassistant

import com.cookingassistant.ui.screens.home.HomeScreen
import com.cookingassistant.ui.screens.login.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cookingassistant.data.network.RetrofitClient
import com.cookingassistant.services.UserService
import com.cookingassistant.ui.screens.home.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Retrofit HTTP Client creation (singleton)
        val apiRepository = RetrofitClient().retrofit
        // Login screen service creation
        val userService = UserService(apiRepository)
        // pass userService to loginViewModel
        val loginViewModel = LoginViewModel(userService)
        setContent {
            AppNavigator(userService) // inject services here
        }
    }
}

@Composable
// modify this code to inject services
fun AppNavigator(userService: UserService){
    val navController = rememberNavController()
    NavGraph(navController = navController, userService = userService)
}

@Composable
fun NavGraph(navController: NavHostController, userService: UserService) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            // create viewModel and inject service
            // TODO: Implement factories later
            //val loginViewModel: LoginViewModel = ViewModelProvider(LoginViewModelFactory(userService))
            val loginViewModel = LoginViewModel(userService)
            LoginScreen(navController, loginViewModel) }
        composable("home") { HomeScreen() }
    }
}

