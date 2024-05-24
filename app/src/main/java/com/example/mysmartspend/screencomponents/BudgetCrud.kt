package com.example.mysmartspend.screencomponents

import com.example.mysmartspend.data.Accounts
import com.example.mysmartspend.data.Budget
import com.example.mysmartspend.data.MaxBudget
import com.example.mysmartspend.data.TempBudgetItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


//These are the Functions used to perform the Crud operations for the Budgeting functionality

fun addTempBudgetItem(item: TempBudgetItem) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("TempBudgetItems").push()
    item.id = ref.key ?: ""
    ref.setValue(item)
}

fun fetchTempBudgetItems(userId: String, onResult: (List<TempBudgetItem>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("TempBudgetItems").orderByChild("userId").equalTo(userId)

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val items = snapshot.children.mapNotNull { it.getValue(TempBudgetItem::class.java) }
            onResult(items)
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(emptyList())
        }
    })
}

fun removeTempBudgetItem(itemId: String) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("TempBudgetItems").child(itemId)
    ref.removeValue()
}

fun storeBudgetTransaction(item: TempBudgetItem) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("BudgetTransactions").push()
    ref.setValue(item)
}

fun saveMaxBudgetAmount(userId: String, maxBudgetAmount: Double) {
    val database = FirebaseDatabase.getInstance().reference
    val maxBudgetRef = database.child("MaxBudgetAmounts").child(userId)
    maxBudgetRef.setValue(MaxBudget(userId, maxBudgetAmount))
}

fun fetchMaxBudgetAmount(userId: String, onResult: (List<MaxBudget>) -> Unit) {
    val database = FirebaseDatabase.getInstance().reference
    val maxBudgetRef = database.child("MaxBudgetAmounts").orderByChild("userId").equalTo(userId)

    maxBudgetRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val budget = snapshot.children.mapNotNull { it.getValue(MaxBudget::class.java) }
                onResult(budget)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(emptyList())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            error.toException().printStackTrace()
            onResult(emptyList())
        }
    })
}

fun fetchBudgetTransactions(userId: String,onResult: (List<Budget>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("BudgetTransactions").orderByChild("userId").equalTo(userId)

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val budgets = snapshot.children.mapNotNull { it.getValue(Budget::class.java) }
            onResult(budgets)
        }

        override fun onCancelled(error: DatabaseError) {
            onResult(emptyList())
        }
    })
}

fun updateAccountBalance(userId: String, newBalance: Double) {
    val database = FirebaseDatabase.getInstance().reference
    val accountRef = database.child("Accounts").child(userId)
    accountRef.child("accountBalance").setValue(newBalance)
}

fun fetchAccountDetails(userId: String, onResult: (Accounts?) -> Unit) {
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








