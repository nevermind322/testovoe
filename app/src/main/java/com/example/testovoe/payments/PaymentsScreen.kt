package com.example.testovoe.payments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsScreen(
    token: String,
    onLogout: () -> Unit,
    vm: PaymentsViewModel = viewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.getPayments(token) }
    LaunchedEffect(vm.isError) {
        if (vm.isError) {
            snackbarHostState.showSnackbar(vm.getMessage())
            vm.isError = false
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = "Easypay") }, actions = {
                IconButton(onClick = onLogout) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "выйти")
                }
            })
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (state) {
                PaymentsScreenUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is PaymentsScreenUiState.Success -> {
                    PaymentsList(list = (state as PaymentsScreenUiState.Success).data)
                }

                else -> Unit
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