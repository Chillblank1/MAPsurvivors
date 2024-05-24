package com.example.mysmartspend.screencomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysmartspend.data.Accounts

//Represents how the bank account details of the user is being displayed on the home screen ui
@Composable
fun AccountSection(account: Accounts?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column (  modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .padding(16.dp)

            ) {

                Column{

                account?.let {
                    Text(
                        text = "Account Type: ${it.accountType}",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Balance:N$${it.accountBalance}",
                        fontSize = 28.sp, color = Color.White
                    )
                } ?: Text(text = "Loading account details...", color = Color.White)
            }
        }
        }
    }
}



