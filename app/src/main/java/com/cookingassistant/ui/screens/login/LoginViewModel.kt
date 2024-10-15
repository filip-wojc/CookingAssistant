package com.cookingassistant.ui.screens.home

import android.net.Network
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(){
    private val _username = MutableStateFlow("") // hold login input
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("") // hold password input
    val password: StateFlow<String> = _password


    fun onUsernameChanged(newUsername:String){
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword:String){
        _password.value = newPassword
    }

    // do wyjebania pozniej
    fun login():Boolean{
        return (_username.value == "user" && _password.value == "password")
    }
}