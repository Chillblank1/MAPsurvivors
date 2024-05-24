package com.example.mysmartspend.screencomponents

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mysmartspend.data.TempBudgetItem


@Composable
fun BudgetSection(maxBudgetAmount: Double, tempBudgetItems: List<TempBudgetItem>, showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
    val totalAmount = tempBudgetItems.sumByDouble { it.amount }
//Launch effect used as side effect to handle  side effects of fetching queries from external database
    LaunchedEffect(totalAmount) {
        if (totalAmount > maxBudgetAmount) {
            setShowDialog(true)
        } else {
            setShowDialog(false)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Budget",
                fontSize = 17.sp,
                color = if (totalAmount > maxBudgetAmount) Color.Red else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "N$ $maxBudgetAmount",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (totalAmount > maxBudgetAmount) Color.Red else MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = "N$ -$totalAmount",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
@Composable
fun TempBudgetItemCard(item: TempBudgetItem, navController: NavController, userId: String, context: Context ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Item: ${item.itemName}", fontSize = 20.sp)
            Text(text = "Category: ${item.category}", fontSize = 16.sp)
            Text(text = "Amount:N$ ${item.amount}", fontSize = 16.sp)
            Row {
                IconButton(onClick = {
                    storeBudgetTransaction(item)
                    displayToast(context, "Item added successfully to Budget list")
                    item.id?.let { removeTempBudgetItem(it) }
                }) {
                    Icon(Icons.Rounded.Check, contentDescription = "Save")
                }
                IconButton(onClick = {
                    item.id?.let { removeTempBudgetItem(it) }
                    displayToast(context, "Item removed successfully")
                }) {
                    Icon(Icons.Rounded.Close, contentDescription = "Delete")
                }
            }
        }
    }
}


private fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

        //Column {
            //Text(
                //text = "Budget",
              //  fontSize = 17.sp,
              //  color = MaterialTheme.colorScheme.onBackground
           // )
            //Spacer(modifier = Modifier.height(8.dp))
           // Text(
             //   text = "$ 44.475",
            //    fontSize = 24.sp,
            //    fontWeight = FontWeight.Bold,
              //  color = MaterialTheme.colorScheme.onBackground
           // )
       // }

       // Box(
           // modifier = Modifier
               // .clip(RoundedCornerShape(15.dp))
               // .background(MaterialTheme.colorScheme.secondaryContainer)
                //.clickable {}
                //.padding(6.dp),
       // ) {
          //  Icon(
             //   imageVector = Icons.Rounded.Notifications,
             //   contentDescription = "Notification",
                //tint = MaterialTheme.colorScheme.onSecondaryContainer
            //)
       // }

   // }
//}