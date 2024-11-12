package com.cookingassistant.data.DTO

data class RegisterRequest(
    val username:String,
    val email:String,
    val password:String
)

data class LoginRequest(
    val email:String,
    val password:String
)

data class LoginResponse(
    val token: String,
    val username: String
)

data class UserPasswordChangeDTO(
    val oldPassword: String?,
    val newPassword: String?,
    val newPasswordConfirm: String?,
)

data class UserDeleteRequest(
    val password: String
)