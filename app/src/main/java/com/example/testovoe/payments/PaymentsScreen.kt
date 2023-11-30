package com.example.testovoe.payments

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PaymentsScreen(token: String, onLogout: () -> Unit, vm: PaymentsViewModel = viewModel()) {
    Column {
        Button(onClick = onLogout) {
            Text("Выйти")
        }
        Text(text = "Your token: $token")
    }
}