package com.example.mysmartspend.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mysmartspend.data.Accounts
import com.example.mysmartspend.data.TempBudgetItem
import com.example.mysmartspend.data.Transaction
import com.example.mysmartspend.screencomponents.BudgetSection
import com.example.mysmartspend.screencomponents.TempBudgetItemCard
import com.example.mysmartspend.screencomponents.addTempBudgetItem
import com.example.mysmartspend.screencomponents.fetchMaxBudgetAmount
import com.example.mysmartspend.screencomponents.fetchTempBudgetItems
import com.example.mysmartspend.screencomponents.fetchTransactions
import com.example.mysmartspend.screencomponents.saveMaxBudgetAmount
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun BudgetScreen(navController: NavController, userId: String) {
    var itemName by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var budgetCategory by remember { mutableStateOf("") }
    var budgetAmount by remember { mutableStateOf("") }
    var tempBudgetItems by remember { mutableStateOf(listOf<TempBudgetItem>()) }
    var account by remember { mutableStateOf<Accounts?>(null) }
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var maxBudgetAmount by remember { mutableStateOf(0.0) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }


//Launch effect used as side effect to handle  side effects of fetching queries from external database
    LaunchedEffect(userId) {
        //Lambda functions were used inorder to execute CRUD Function
        fetchAccountDetail(userId) { fetchedAccount ->
            account = fetchedAccount
            fetchedAccount?.let {
                fetchTransactions(it.accountId) { fetchedTransactions ->
                    transactions = fetchedTransactions
                }
            }
        }
        fetchMaxBudgetAmount(userId) { fetchedAmount ->
            maxBudgetAmount = fetchedAmount.firstOrNull()?.amount ?: 0.0
        }
        fetchTempBudgetItems(userId) { fetchedTempBudgetItems ->
            tempBudgetItems = fetchedTempBudgetItems
        }
    }

//Ui for Budget screen along wiit columns and text being used
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(text = "Budget Plan", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.height(28.dp))
        OutlinedTextField(
            value = maxBudgetAmount.toString(),
            onValueChange = {
                maxBudgetAmount = it.toDoubleOrNull() ?: 0.0
                if (account?.accountBalance != null && maxBudgetAmount <= account!!.accountBalance) {
                    saveMaxBudgetAmount(userId, maxBudgetAmount)
                }
            },
            label = { Text("Max Budget Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        account?.let {
            if (maxBudgetAmount > it.accountBalance) {
                Text(
                    text = "The budget should not exceed the account balance",
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        BudgetSection(maxBudgetAmount, tempBudgetItems, showDialog) { showDialog = it }

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = budgetCategory,
            onValueChange = { budgetCategory = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = budgetAmount,
            onValueChange = { budgetAmount = it },
            label = { Text("Budget Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            onClick = {
                val newItem = TempBudgetItem(id, userId, itemName, budgetCategory, budgetAmount.toDouble())
                addTempBudgetItem(newItem)
                displayToast(context, "Item added successfully to Item list")
                itemName = ""
                budgetCategory = ""
                budgetAmount = ""
            },
            enabled = itemName.isNotEmpty() && budgetCategory.isNotEmpty() && budgetAmount.toDoubleOrNull()?.let { it <= maxBudgetAmount } == true,
            modifier = Modifier.fillMaxWidth().padding(8.dp),colors= ButtonDefaults.buttonColors(Color.LightGray)
        ) {
            Text("Add Budget Item")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                },
                text = {
                    Text("The total budget should not exceed the set max budget.")
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            tempBudgetItems.forEach { item ->
                TempBudgetItemCard(item, navController, userId,context)
            }
        }
    }
}

fun fetchAccountDetail(userId: String, onResult: (Accounts?) -> Unit,) {

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Accounts").orderByChild("userId").equalTo(userId)

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (accountSnapshot in snapshot.children) {
                val account = accountSnapshot.getValue(Accounts::class.java)
                onResult(account)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(null)
        }
    })
}

private fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

