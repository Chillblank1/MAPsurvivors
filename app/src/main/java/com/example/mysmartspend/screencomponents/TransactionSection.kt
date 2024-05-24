package com.example.mysmartspend.screencomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysmartspend.data.Transaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


//represents how the data for transactions is displayed on the ui
@Composable
fun TransactionSection(transactions: List<Transaction>) {
    Column {
        Text(text = "Transaction History", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        if (transactions.isEmpty()) {
            Text(text = "No transactions found.")
        } else {
            LazyColumn {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

//represents how the data for transactions is displayed on the ui
@Composable
fun TransactionItem(transaction: Transaction) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Date Column
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.transactionDateTime,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                )
            )
        }

        // Description Column
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Amount Column
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (transaction.transactionType == "Expense") {
                    "-$${transaction.transactionAmount}"
                } else {
                    "+$${transaction.transactionAmount}"
                },
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
//function used to fetch  the transactions for specific user using the userId
fun fetchTransactions(accountId: Int, onResult: (List<Transaction>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("Transactions").orderByChild("accountId").equalTo(accountId.toDouble())

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val transactions = mutableListOf<Transaction>()
            for (transactionSnapshot in snapshot.children) {
                val transaction = transactionSnapshot.getValue(Transaction::class.java)
                transactions.add(transaction!!)
            }
            onResult(transactions)
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(emptyList())
        }
    })
}