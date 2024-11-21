package com.cookingassistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cookingassistant.compose.AppTheme
import com.cookingassistant.data.network.RetrofitClient
import com.cookingassistant.data.objects.ScreenControlManager
import com.cookingassistant.data.repositories.TokenRepository
import com.cookingassistant.services.AuthService
import com.cookingassistant.services.RecipeService
import com.cookingassistant.services.ReviewService
import com.cookingassistant.services.UserService
import com.cookingassistant.ui.composables.topappbar.TopAppBar
import com.cookingassistant.ui.composables.topappbar.TopAppBarViewModel
import com.cookingassistant.ui.screens.RecipesList.RecipeList
import com.cookingassistant.ui.screens.RecipesList.RecipesListViewModel
import com.cookingassistant.ui.screens.editor.EditorScreen
import com.cookingassistant.ui.screens.editor.EditorScreenViewModel
import com.cookingassistant.ui.screens.editor.authorization.AuthorizationScreen
import com.cookingassistant.ui.screens.editor.authorization.AuthorizationScreenViewModel
import com.cookingassistant.ui.screens.home.HomeScreen
import com.cookingassistant.ui.screens.home.HomeScreenViewModel
import com.cookingassistant.ui.screens.home.LoginViewModel
import com.cookingassistant.ui.screens.login.LoginScreen
import com.cookingassistant.ui.screens.profile.ProfileScreen
import com.cookingassistant.ui.screens.profile.ProfileScreenViewModel
import com.cookingassistant.ui.screens.recipescreen.RecipeScreen
import com.cookingassistant.ui.screens.recipescreen.RecipeScreenViewModel
import com.cookingassistant.ui.screens.registration.RegistrationScreen
import com.cookingassistant.ui.screens.registration.RegistrationViewModel
import com.cookingassistant.ui.screens.reviews.ReviewList
import com.cookingassistant.ui.screens.reviews.ReviewViewModel
import com.cookingassistant.util.VoiceToTextParser
import java.io.File


class MainActivity : ComponentActivity() {
    // Declare the permission request contract
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, proceed with your logic
            println("Permission granted")
        } else {
            // Permission denied, handle accordingly
            println("Permission denied")
        }
    }

    val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check and request permission if not already granted
        checkAndRequestPermission()

        //Retrofit HTTP Client creation (singleton)
        val tokenRepository = TokenRepository(applicationContext)
        val apiRepository = RetrofitClient(tokenRepository).retrofit
        // Create services
        val authService = AuthService(apiRepository)
        val userService = UserService(apiRepository)
        val reviewService = ReviewService(apiRepository)
        val recipeService = RecipeService(apiRepository)
        // Pdf directory
        val destinationDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        setContent {
            var canRecord by remember {
                mutableStateOf(false)
            }

            val recordAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = {isGranted ->
                    canRecord = isGranted
                }
            )
            LaunchedEffect(key1 = recordAudioLauncher) {
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            AppNavigator(authService,userService,reviewService,recipeService ,tokenRepository, destinationDir, voiceToTextParser) // inject services here
        }
    }

    override fun onBackPressed() { //Deprecated but wbo asked?
        if (ScreenControlManager.activeTool == "") {
            if(ScreenControlManager.hasLoggedIn && ScreenControlManager.topAppBarViewModel.onAppTryExit()) {
                moveTaskToBack(true)
            } else {
                super.onBackPressed()
            }
        } else {
            ScreenControlManager.topAppBarViewModel.onDeselctTool()
        }
    }

    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) ==
                        PackageManager.PERMISSION_GRANTED -> {

                    println("Permission already granted")
                }
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES) -> {

                    println("Need permission to access images")
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
                else -> {

                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
        } else {

            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted, proceed with file access
                    println("Permission already granted")

                }
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // Show rationale if necessary and request permission
                    println("Need permission to access storage")
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                else -> {
                    // Request the permission
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }


    }
}

@Composable
// modify this code to inject services
fun AppNavigator(authService: AuthService,userService: UserService,reviewService: ReviewService, recipeService: RecipeService, tokenRepository: TokenRepository, destinationDir: File, voiceToTextParser: VoiceToTextParser){
    val navController = rememberNavController()
    NavGraph(navController = navController,
        authService = authService,
        userService = userService,
        reviewService = reviewService,
        recipeService = recipeService,
        tokenRepository = tokenRepository,
        destinationDir = destinationDir,
        voiceToTextParser = voiceToTextParser)
}

@Composable
fun NavGraph(navController: NavHostController, authService: AuthService, userService: UserService,reviewService: ReviewService,recipeService: RecipeService, tokenRepository: TokenRepository, destinationDir: File, voiceToTextParser: VoiceToTextParser) {
    AppTheme(true) {
        val reviewViewModel = ReviewViewModel(reviewService)
        val rsvm = RecipeScreenViewModel(recipeService, userService, reviewService, navController, reviewViewModel)
        val esvm = EditorScreenViewModel(recipeService)
        val recipeListViewModel = RecipesListViewModel(recipeService,userService)
        val homeScreenViewModel = HomeScreenViewModel(recipeService, rsvm, navController)
        val pvm = ProfileScreenViewModel(userService)
        val topBarViewModel = TopAppBarViewModel(recipeService, rsvm, navController, recipeListViewModel, voiceToTextParser,pvm,esvm)
        ScreenControlManager.topAppBarViewModel=topBarViewModel
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                // create viewModel and inject service
                // TODO: Implement factories later
                //val loginViewModel: LoginViewModel = ViewModelProvider(LoginViewModelFactory(userService))
                val loginViewModel = LoginViewModel(authService, tokenRepository)
                LoginScreen(navController, loginViewModel)
            }
            composable("home") {
                TopAppBar(topAppBarviewModel = topBarViewModel) {
                    HomeScreen(homeScreenViewModel)
                }
            }
            composable("test") {//For testing purposes
                //val reviewViewModel = ReviewViewModel(reviewService)
                //ReviewList(reviewViewModel)
            }

            composable("recipeReviews") {
                TopAppBar(topAppBarviewModel = topBarViewModel) {
                    ReviewList(reviewViewModel)
                }
            }

            composable("recipeList") {
                TopAppBar(topAppBarviewModel = topBarViewModel) {
                    RecipeList(navController,rsvm,esvm,recipeListViewModel)
                }
            }
            composable("registration") {
                val registrationViewModel = RegistrationViewModel(authService)
                RegistrationScreen(navController, registrationViewModel)
            }
            composable("recipeScreen") {
                TopAppBar(topAppBarviewModel = topBarViewModel) {
                    RecipeScreen(rsvm, destinationDir)
                }
            }
            composable("editor"){
                TopAppBar(topAppBarviewModel = topBarViewModel) {
                    EditorScreen(navController, esvm)
                }
            }
            composable("profile"){
                TopAppBar(topAppBarviewModel = topBarViewModel) {
                    ProfileScreen(navController,recipeListViewModel,pvm)
                }
            }
            composable("authorization"){
                val avm = AuthorizationScreenViewModel(userService)
                AuthorizationScreen(navController,avm)
            }
        }
    }
}

