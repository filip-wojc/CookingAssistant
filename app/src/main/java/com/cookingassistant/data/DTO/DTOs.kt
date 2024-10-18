package com.cookingassistant.data.DTO

data class RegisterRequest( val userName:String, val email:String,val password:String)
data class LoginRequest(val email:String,val password:String)
data class LoginResponse(val token: String)
