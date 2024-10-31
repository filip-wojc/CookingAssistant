package com.cookingassistant.data.DTO

data class RegisterRequest( val username:String, val email:String,val password:String)
// RESPONSE : OK() or BadRequest() - no need for DTO

data class LoginRequest(val email:String,val password:String)
data class LoginResponse(val token: String)
