package com.example.mysmartspend.data

data class User(
    val userId: String? =null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val accountNumber: String =""
)
