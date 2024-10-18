package com.cookingassistant.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookingassistant.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(private val _service: UserService) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    // Hold login and password input
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    // Handle login result (token or error message)
    private val _registrationResult = MutableStateFlow<String?>(null)
    val registrationResult: StateFlow<String?> = _registrationResult

    // Registration success
    private val _isRegistrationSucessful = MutableStateFlow<Boolean>(false)
    val isRegistrationSuccessful: StateFlow<Boolean> = _isRegistrationSucessful

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

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun registerUser() {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            try {
                val response = _service.registerUser(username.value, email.value, password.value)
                if (response.isSuccessful) {
                    _isRegistrationSucessful.value = true
                    _registrationResult.value = "Registration Successful"
                } else {
                    val errorBody = response.errorBody()?.string()
                    _isRegistrationSucessful.value = false
                    _registrationResult.value = errorBody ?: "Registration failed: ${response.message()}" // Update with error message
                }

            } catch (e: Exception) {
                _isRegistrationSucessful.value = false
                _registrationResult.value = "Error: ${e.message}" // Handle exceptions

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
