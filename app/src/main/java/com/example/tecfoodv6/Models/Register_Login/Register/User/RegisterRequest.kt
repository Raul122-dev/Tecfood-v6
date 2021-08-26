package com.example.tecfoodv6.Models.Register_Login.Register.User

data class RegisterRequest (
    var username: String,
    var first_name: String,
    var last_name: String,
    var email: String,
    var password: String)