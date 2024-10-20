package com.cookingassistant.ui.screens.login
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import coil3.compose.AsyncImage
import com.cookingassistant.ui.screens.home.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val loginResult by loginViewModel.loginResult.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val isDialogVisible by loginViewModel.isDialogVisible.collectAsState()
    val isLoginSuccessful by loginViewModel.isLoginSuccessful.collectAsState()
    val isPasswordVisible by loginViewModel.isPasswordVisible.collectAsState()
    val textFieldState = remember { TextFieldState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Username input
        TextField(
            value = username,
            onValueChange = { loginViewModel.onUsernameChanged(it) },
            label = { Text("Login") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = { loginViewModel.onPasswordChanged(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { loginViewModel.onPasswordVisibilityChange() }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login button
        Button(
            onClick = {
                loginViewModel.login()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in")
        }

        // Go to registration page button
        Button(
            onClick = {
                navController.navigate("registration")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        // Show result (either token or error message) in a dialog
        if (loginResult != null) {
            AlertDialog(
                onDismissRequest = { /* Dismiss the dialog */ },
                title = { Text("Login Result") },
                text = { Text(loginResult ?: "") },
                confirmButton = {
                    Button(onClick = {
                        loginViewModel.hideResultDialog()
                        if(isLoginSuccessful) {
                            navController.navigate("home")
                        }
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

