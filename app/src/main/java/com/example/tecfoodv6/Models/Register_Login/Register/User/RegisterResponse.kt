package com.example.tecfoodv6.Models.Register_Login.Register.User

import com.example.tecfoodv6.Models.User.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val user: UserResponse,
    val token:String,
)