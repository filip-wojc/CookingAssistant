package com.cookingassistant

import com.cookingassistant.ui.screens.home.HomeScreen
import com.cookingassistant.ui.screens.login.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cookingassistant.ui.theme.CookingAssistantTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cookingassistant.data.network.RetrofitClient
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.cookingassistant.data.network.ApiRepository
import com.cookingassistant.ui.screens.home.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiService = RetrofitClient().retrofit
        val apiRepository = ApiRepository(apiService)
        val loginViewModel = LoginViewModel(apiRepository)
        setContent {
            AppNavigator(loginViewModel)
        }
    }
}

@Composable
fun AppNavigator(loginViewModel: LoginViewModel){
    val navController = rememberNavController()
    NavGraph(navController = navController, loginViewModel = loginViewModel)
}

@Composable
fun NavGraph(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel = loginViewModel ) }
        composable("home") { HomeScreen() }
    }
}

