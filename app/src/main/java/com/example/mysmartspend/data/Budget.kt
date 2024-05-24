package com.example.mysmartspend.data

data class TempBudgetItem(
    var id: String? = null,
    val userId: String = "",
    val itemName: String = "",
    val category: String="",
    val amount: Double = 0.0
)

data class Budget(
    val id: String = "",
    val userId: String = "",
    val itemName: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val timestamp: Long = 0L
)

data class MaxBudget(
    val userId: String="",
    val amount: Double=0.0
)