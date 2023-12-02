package com.example.testovoe.payments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentsViewModel(private val paymentsRepository: PaymentsRepository = PaymentsRepository()) :
    ViewModel() {

    private val _state = MutableStateFlow<PaymentsScreenUiState>(PaymentsScreenUiState.Loading)
    val state = _state.asStateFlow()

    var isError by mutableStateOf(false)

    var loadJob: Job? = null

    fun getPayments(token: String) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _state.value = PaymentsScreenUiState.Loading
            _state.value = when (val res = paymentsRepository.getPayments(token)) {
                is RepoResult.Error -> PaymentsScreenUiState.Error(res.msg).also { isError = true }
                is RepoResult.Success -> PaymentsScreenUiState.Success(res.data)
            }
        }
    }

    fun getMessage(): String = with(state.value) {
        when (this) {
            is PaymentsScreenUiState.Error -> message
            else -> "Exception: Unknown error!"
        }
    }
}


sealed class PaymentsScreenUiState {
    data object Loading : PaymentsScreenUiState()
    data class Success(val data: List<Payment>) : PaymentsScreenUiState()
    data class Error(val message: String) : PaymentsScreenUiState()
}