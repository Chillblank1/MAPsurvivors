package com.example.mysmartspend.screens


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.mysmartspend.data.Accounts
import com.example.mysmartspend.data.Budget
import com.example.mysmartspend.data.Transaction
import com.example.mysmartspend.screencomponents.AccountSection
import com.example.mysmartspend.screencomponents.Logout
import com.example.mysmartspend.screencomponents.TransactionSection
import com.example.mysmartspend.screencomponents.fetchAccountDetails
import com.example.mysmartspend.screencomponents.fetchBudgetTransactions
import com.example.mysmartspend.screencomponents.fetchTransactions
import com.example.mysmartspend.screencomponents.updateAccountBalance


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(userId: String) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var account by remember { mutableStateOf<Accounts?>(null) }
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var budgetTransactions by remember { mutableStateOf<List<Budget>>(emptyList()) }
    var updatedBalance by remember { mutableStateOf(0.0) }

//Launch effect used as side effect to handle  side effects of fetching queries from external database
    LaunchedEffect(userId) {
        //Lambda functions are used inorder to execute these CRUD Functions
        fetchAccountDetails(userId) { fetchedAccount ->
            account = fetchedAccount
            fetchedAccount?.let {
                fetchTransactions(it.accountId) { fetchedTransactions ->
                    transactions = fetchedTransactions
                    fetchBudgetTransactions(userId) { fetchedBudgetTransactions ->
                        budgetTransactions = fetchedBudgetTransactions
                        val BudgetTotal= budgetTransactions.sumByDouble { it.amount }
                        updatedBalance = it.accountBalance -BudgetTotal
                        updateAccountBalance(it.userId, updatedBalance)
                    }
                }
            }
        }
    }


    //Used scaffolding in the ui for better app layout structure
    Scaffold(
    )
        { padding ->


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Text(text = "Home", fontSize = 24.sp, modifier = Modifier.padding(8.dp))


                account?.let { AccountSection(it) }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Updated Balance: $updatedBalance")
                Spacer(modifier = Modifier.height(16.dp))
                TransactionSection(transactions)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "BudgetList", fontSize = 23.sp)
                LazyColumn {
                    items(budgetTransactions) { budgetTransactions ->
                        BudgetTransactionItem(budgetTransactions,context)
                    }
                }
            }
        }

}


//Represents how the budget list will be presented on the ui of the home screen
@Composable
fun BudgetTransactionItem(budgetTransactions: Budget,context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Transaction: ${budgetTransactions.itemName}", fontSize = 20.sp, color = Color.White)
            Text(text = "Category: ${budgetTransactions.category}", fontSize = 16.sp, color = Color.White)
            Text(text = "Amount: N$-${budgetTransactions.amount}", fontSize = 16.sp, color = Color.White)
        }
        }
    }










