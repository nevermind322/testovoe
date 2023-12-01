package com.example.testovoe.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testovoe.api.EasypayService
import com.example.testovoe.api.ServerLoginResponse
import com.example.testovoe.api.apiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val api: EasypayService = apiClient) : ViewModel() {

    private val _state = MutableStateFlow<LoginScreenUiState>(LoginScreenUiState.Start)
    val state = _state.asStateFlow()

    fun resetState() {
        viewModelScope.launch {
            _state.value = LoginScreenUiState.Start
        }
    }

    fun authorize(creds: Credentials) {
        viewModelScope.launch {
            _state.value = LoginScreenUiState.Loading
            val res = try {
                when (val answer = api.authorize(creds)) {
                    is ServerLoginResponse.Success -> LoginScreenUiState.Success(answer.response.token)
                    is ServerLoginResponse.Error -> LoginScreenUiState.Error(
                        answer.error.code, answer.error.error
                    )
                }
            } catch (e: Throwable) {
                LoginScreenUiState.Exception(e)
            }
            _state.value = res
        }
    }
}

sealed class LoginScreenUiState() {
    data object Start : LoginScreenUiState()
    data object Loading : LoginScreenUiState()
    data class Success(val token: String) : LoginScreenUiState()
    data class Exception(val e: Throwable) : LoginScreenUiState()
    data class Error(val code: Int, val msg: String) : LoginScreenUiState()
}