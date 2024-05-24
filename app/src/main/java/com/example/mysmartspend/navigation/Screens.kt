package com.example.mysmartspend.navigation

//this is a sealed class for the screen navigation
sealed class Screens (val screen: String){
    object SignIn : Screens("signin")
    object Home : Screens("home/{userId}")
    object Budget : Screens("budget")
    object BudgetDetail : Screens("budgetDetail/{category}")
    object Account : Screens("account")


}