package com.cookingassistant.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.data.repositories.TokenRepository
import com.cookingassistant.services.AuthService
import com.cookingassistant.data.Models.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val _service: AuthService, private val tokenRepository: TokenRepository) : ViewModel() {

    private val _ingredientsList = MutableStateFlow<List<Pair<String,Int>>?>(null)
    val ingredientsList:StateFlow<List<Pair<String,Int>>?> = _ingredientsList
    // Hold login and password input
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    // Login success tracker
    private val _isLoginSuccessful = MutableStateFlow<Boolean>(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    // Handle loading state during login request
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Result dialog handling variable
    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onPasswordVisibilityChange() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

   fun login() {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            try {
                val result = _service.logInUser(username.value, password.value)

                if (result is Result.Success) {
                    val token = result.data!!.token
                    tokenRepository.saveToken(token)
                    _isLoginSuccessful.value = true
                    _loginResult.value = "Login successful" // Update with token if successful

                }
                else if (result is Result.Error ) {
                    _isLoginSuccessful.value = false
                    _loginResult.value = "Login failed: ${result.message}" // Update with error message
                    result.detailedErrors?.forEach { field, messages ->
                        messages.forEach { message ->
                            Log.d("login", "$field: $message")
                        }
                    }
                }
            } catch (e: Exception) {
                _loginResult.value = "Error: ${e.message}" // Handle exceptions
                _isLoginSuccessful.value = false
            } finally {
                _isLoading.value = false // Stop loading
                _isDialogVisible.value = true
            }
        }
    }

    fun showResultDialog() {
        _isDialogVisible.value = true
    }

    fun hideResultDialog() {
        _isDialogVisible.value = false
    }
}
