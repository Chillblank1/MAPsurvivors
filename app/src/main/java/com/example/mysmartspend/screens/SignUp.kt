package com.example.mysmartspend.screens

import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.firebase.database.*

@Composable
fun SignUpScreen(navController: NavController) {
    // Get instance of Firebase database and references to users and accounts
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val accountsRef = database.getReference("Accounts")

    // State variables for user input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Get current context for displaying Toast messages
    val context = LocalContext.current

    // Main UI layout
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Vertically centered column for input fields and buttons
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title text
            Text(
                text = "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.heightIn(min = 40.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Input field for first name
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "First Name") }
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Input field for last name
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Last Name") }
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Input field for email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") }
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Input field for password with visual transformation
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Input field for account number
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text(text = "Account Number") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign up button
            Button(
                onClick = {
                    if (isLoading) return@Button // Prevent multiple clicks while loading
                    isLoading = true
                    when {
                        // Check if fields are empty and show appropriate Toast message
                        TextUtils.isEmpty(firstName) -> {
                            Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(lastName) -> {
                            Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(email) -> {
                            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(password) -> {
                            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(accountNumber) -> {
                            Toast.makeText(context, "Please enter account number", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Check if email is unique
                            usersRef.orderByChild("email").equalTo(email)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Toast.makeText(context, "Please use a unique email e.g example17@gmail.com", Toast.LENGTH_SHORT).show()
                                            isLoading = false
                                        } else {
                                            // Check if account number exists
                                            accountsRef.orderByChild("accountNumber").equalTo(accountNumber)
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onDataChange(accountSnapshot: DataSnapshot) {
                                                        if (accountSnapshot.exists()) {
                                                            // Retrieve userId from user account
                                                            val accountData = accountSnapshot.children.iterator().next()
                                                            val userId = accountData.child("userId").getValue(String::class.java) ?: ""

                                                            // Create new user with the retrieved userId
                                                            val newUser = User(userId, firstName, lastName, email, password, accountNumber)
                                                            usersRef.push().setValue(newUser).addOnCompleteListener { task ->
                                                                if (task.isSuccessful) {
                                                                    Toast.makeText(context, "User successfully registered", Toast.LENGTH_SHORT).show()
                                                                    navController.navigate("signin")
                                                                } else {
                                                                    Toast.makeText(context, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                                                                }
                                                                isLoading = false
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Invalid account number. Please provide a valid account number.", Toast.LENGTH_SHORT).show()
                                                            isLoading = false
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        Toast.makeText(context, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                                                        isLoading = false
                                                    }
                                                })
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(context, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                                        isLoading = false
                                    }
                                })
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text for navigation to sign in screen
            Text(
                text = "Already Have an Account? Sign In",
                modifier = Modifier.clickable {
                    navController.navigate("signin")
                }
            )
        }
    }
}
