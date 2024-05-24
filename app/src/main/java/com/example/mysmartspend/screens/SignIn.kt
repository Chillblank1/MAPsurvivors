package com.example.mysmartspend.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mysmartspend.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun SignInScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current// Get the current context for Toast messages

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to SmartSpend",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.heightIn(min = 40.dp)
            )
            Text(
                text = "Sign In",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.heightIn()
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") }
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {// Check if email or password is empty
                        Toast.makeText(context, "Please enter all credentials", Toast.LENGTH_SHORT).show()
                    } else {
                        val firebaseDatabase = FirebaseDatabase.getInstance()// Get instance of Firebase database
                        val usersRef = firebaseDatabase.reference.child("users")// Reference to "users" node in Firebase
                        usersRef.orderByChild("email").equalTo(email)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {// If user with email exists
                                        for (userSnapshot in dataSnapshot.children) {
                                            val userData = userSnapshot.getValue(User::class.java)
                                            if (userData != null && userData.password == password) {
                                                Toast.makeText(context, "User Successfully signed in", Toast.LENGTH_SHORT).show()
                                                val userId = userData.userId
                                                email = ""
                                                password = ""
                                                navController.navigate("home/${userId}") // Pass userId as a parameter to the home screen
                                                return
                                            }
                                        }

                                    } else {
                                        Toast.makeText(context, "Please enter the correct credentials", Toast.LENGTH_SHORT).show()

                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text(text = "Sign In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Don't Have an Account? Sign Up", modifier = Modifier.clickable {
                navController.navigate("signup")
            })
        }
    }
}
