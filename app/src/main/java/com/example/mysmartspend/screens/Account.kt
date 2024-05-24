package com.example.mysmartspend.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mysmartspend.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun AccountScreen( userId: String) {
    //state variables for each textfield in ui
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    val context= LocalContext.current

//Launch effect used as side effect to handle  side effects of fetching queries from external database
    LaunchedEffect(userId) {
        fetchUserDetails(userId) { user ->
            firstName = user.firstName
            lastName = user.lastName
            email = user.email
            password = user.password
            accountNumber = user.accountNumber
        }
    }
    Text(text = "Edit User Details", fontSize = 24.sp, modifier = Modifier.padding(8.dp))


    //column with the necessary textfields for the updating of the user
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile",modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = userId,
            onValueChange = {},
            label = { Text("User ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            enabled = false
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = accountNumber,
            onValueChange = {},
            label = { Text("Account Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            enabled = false
        )

        Button(
            onClick = {
                updateUserDetails(context,userId, firstName, lastName, email, password, accountNumber)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),colors= ButtonDefaults.buttonColors(Color.LightGray)
        ) {
            Text("Update Details")
        }
    }
}

fun fetchUserDetails(userId: String, callback: (User) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val user = dataSnapshot.getValue(User::class.java)
            if (user != null) {
                callback(user)
            } else {
                callback(User())
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callback(User())
        }
    })
}



fun updateUserDetails(
    context: Context,
    userId: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    accountNumber: String
) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users").child(userId)

    val updatedUser = User(userId, firstName, lastName, email, password, accountNumber)
    usersRef.setValue(updatedUser)
        .addOnSuccessListener {
            displayToast(context, "Successfully Updated User Details")
        }
        .addOnFailureListener {
            displayToast(context, "Failed to Update User Details")
        }
}

private fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}