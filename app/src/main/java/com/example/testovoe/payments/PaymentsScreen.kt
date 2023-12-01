package com.example.testovoe.payments

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PaymentsScreen(
    token: String,
    onLogout: () -> Unit,
    snackbarHostState: SnackbarHostState,
    vm: PaymentsViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.getPayments(token)
    }
    val scope = rememberCoroutineScope()
    val state by vm.state.collectAsState()
    Column {
        Button(onClick = onLogout) {
            Text("Выйти")
        }
        Button(onClick = { vm.getPayments(token) }) {
            Text("Repeat")
        }

        when (state) {
            is PaymentsScreenUiState.Error -> {
                /* LaunchedEffect(state) {
                     scope.launch {
                         snackbarHostState.showSnackbar((state as PaymentsScreenUiState.Error).message)
                     }
                 }*/
                Toast.makeText(
                    LocalContext.current,
                    (state as PaymentsScreenUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            PaymentsScreenUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PaymentsScreenUiState.Success -> {
                PaymentsList(list = (state as PaymentsScreenUiState.Success).data)
            }
        }

    }
}


@Composable
fun PaymentsList(list: List<Payment>) {

    LazyColumn() {
        items(list) {
            PaymentElement(payment = it)
        }
    }

}

@Composable
fun PaymentElement(payment: Payment) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(payment.title)
        if (payment.amount != null)
            Text(text = payment.amount.toString())
    }
}