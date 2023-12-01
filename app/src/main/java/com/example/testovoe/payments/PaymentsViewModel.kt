package com.example.testovoe.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentsViewModel(private val paymentsRepository: PaymentsRepository = PaymentsRepository()) :
    ViewModel() {

    private val _state = MutableStateFlow<PaymentsScreenUiState>(PaymentsScreenUiState.Loading)
    val state = _state.asStateFlow()
    fun getPayments(token: String) {
        viewModelScope.launch {
            _state.value = PaymentsScreenUiState.Loading
            _state.value = when (val res = paymentsRepository.getPayments(token)) {
                is RepoResult.Error -> PaymentsScreenUiState.Error(res.msg)
                is RepoResult.Success -> PaymentsScreenUiState.Success(res.data)
            }
        }
    }
}


sealed class PaymentsScreenUiState {
    data object Loading : PaymentsScreenUiState()
    data class Success(val data: List<Payment>) : PaymentsScreenUiState()
    data class Error(val message: String) : PaymentsScreenUiState()
}