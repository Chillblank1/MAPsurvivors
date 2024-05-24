package com.example.mysmartspend.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mysmartspend.screencomponents.BottomNavBar
import com.example.mysmartspend.screens.SignInScreen
import com.example.mysmartspend.screens.SignUpScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signin") {
        composable("signin") { SignInScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("home/{userId}") {

            //allows the app to keep track of the state of the screen,inlcuding the parameters passed to it
            //Makes sure that the data is passed to the screen in a correct manner in our
            // it will make sure the correct userId is passed to the Bottom navigation bar
            //allows for userId to be remembered across the different screens
                backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
        BottomNavBar(userId.toString())
        }

    }
}




