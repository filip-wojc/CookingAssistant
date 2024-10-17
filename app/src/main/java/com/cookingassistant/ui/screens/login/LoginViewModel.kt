package com.cookingassistant.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserService) : ViewModel() {

    // Hold login and password input
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // Handle login result (token or error message)
    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    // Handle loading state during login request
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            try {
                val response = repository.logInUser(username.value, password.value)
                if (response.isSuccessful) {
                    _loginResult.value = response.body()?.token // Update with token if successful
                } else {
                    _loginResult.value = "Login failed: ${response.message()}" // Update with error message
                }
            } catch (e: Exception) {
                _loginResult.value = "Error: ${e.message}" // Handle exceptions
            } finally {
                _isLoading.value = false // Stop loading
            }
        }
    }
}
