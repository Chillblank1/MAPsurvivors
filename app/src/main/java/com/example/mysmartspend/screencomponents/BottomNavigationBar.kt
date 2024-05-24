package com.example.mysmartspend.screencomponents

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mysmartspend.navigation.Screens
import com.example.mysmartspend.screens.AccountScreen
import com.example.mysmartspend.screens.BudgetScreen
import com.example.mysmartspend.screens.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavBar(userId: String) {

    //used navhost for the navigation of screens
    val navController = rememberNavController()
    val selected = remember { mutableStateOf(Screens.Home.screen) }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                IconButton(
                    onClick = {
                        selected.value = Screens.Home.screen
                        navController.navigate(Screens.Home.screen.replace("{userId}", userId)) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.Home, contentDescription = null,
                        tint = if (selected.value == Screens.Home.screen) Color.White else Color.DarkGray
                    )
                }
                IconButton(
                    onClick = {
                        selected.value = Screens.Budget.screen
                        navController.navigate(Screens.Budget.screen) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.AccountBalanceWallet, contentDescription = null,
                        tint = if (selected.value == Screens.Budget.screen) Color.White else Color.DarkGray
                    )
                }
                IconButton(
                    onClick = {
                        selected.value = Screens.Account.screen
                        navController.navigate(Screens.Account.screen) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle, contentDescription = null,
                        tint = if (selected.value == Screens.Account.screen) Color.White else Color.DarkGray
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen.replace("{userId}", userId),
            modifier = Modifier.padding(paddingValues)
        ) {

            //refer back to the screens data class in navigation package
            composable(Screens.Home.screen) { HomeScreen(userId) }
            composable(Screens.Budget.screen) { BudgetScreen(navController,userId) }
            composable(Screens.Account.screen) { AccountScreen(userId) }
        }
    }
}