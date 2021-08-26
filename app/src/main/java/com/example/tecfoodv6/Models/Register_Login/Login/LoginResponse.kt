package com.example.tecfoodv6.Models.Register_Login.Login

import com.example.tecfoodv6.Models.User.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    val expiry: String,
    val token:String,
    val Usuario: List<UserResponse> )