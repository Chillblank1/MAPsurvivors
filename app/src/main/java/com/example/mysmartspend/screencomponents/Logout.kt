package com.example.mysmartspend.screencomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mysmartspend.navigation.Screens




//The logout button is used to navigate back to the  sigin screen ,back before that
// it confirms  that the user really wants to logout via alert dialog
@Composable
fun Logout(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { showDialog = true }
            .padding(6.dp),
    ) {
        IconButton(onClick = { showDialog = true }) {
            Icon(Icons.Rounded.Logout, contentDescription = "Logout")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirm Logout") },
                text = { Text(text = "Are you sure you want to log out?") },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        // Perform logout actions here, such as clearing user data
                        navController.navigate(Screens.SignIn.screen) {
                            popUpTo(Screens.Home.screen) { inclusive = true }
                        }
                    }) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
