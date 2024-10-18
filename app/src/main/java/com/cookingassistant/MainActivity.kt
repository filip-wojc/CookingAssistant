package com.cookingassistant

import android.media.session.MediaSession.Token
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
import com.cookingassistant.data.TokenRepository
import com.cookingassistant.data.network.RetrofitClient
import com.cookingassistant.services.UserService
import com.cookingassistant.ui.screens.home.LoginViewModel
import com.cookingassistant.ui.screens.registration.RegistrationScreen
import com.cookingassistant.ui.screens.registration.RegistrationViewModel
import com.cookingassistant.ui.screens.RecipesList.TestRecipesColumn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Retrofit HTTP Client creation (singleton)
        val apiRepository = RetrofitClient().retrofit
        // Login screen service creation
        val userService = UserService(apiRepository)
        val tokenRepository = TokenRepository(applicationContext)
        setContent {
            AppNavigator(userService, tokenRepository) // inject services here
        }
    }
}

@Composable
// modify this code to inject services
fun AppNavigator(userService: UserService, tokenRepository:TokenRepository){
    val navController = rememberNavController()
    NavGraph(navController = navController,
        tokenRepository = tokenRepository,
        userService = userService)
}

@Composable
fun NavGraph(navController: NavHostController,tokenRepository: TokenRepository, userService: UserService) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            // create viewModel and inject service
            // TODO: Implement factories later
            //val loginViewModel: LoginViewModel = ViewModelProvider(LoginViewModelFactory(userService))
            val loginViewModel = LoginViewModel(userService, tokenRepository)
            LoginScreen(navController, loginViewModel) }
        composable("home") { HomeScreen() }

        composable("registration"){
            val registrationViewModel = RegistrationViewModel(userService)
            RegistrationScreen(navController, registrationViewModel)
        }
    }
}

