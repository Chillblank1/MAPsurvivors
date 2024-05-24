package com.example.mysmartspend.data

data class Transaction(
    val accountId: Int = 0,
    val description: String = "",
    val transactionAmount: Int = 0,
    val transactionDateTime: String = "",
    val transactionId: Int = 0,
    val transactionType: String = ""
)