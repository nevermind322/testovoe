package com.example.testovoe.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testovoe.api.EasypayService
import com.example.testovoe.api.ServerLoginResponse
import com.example.testovoe.api.apiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val api: EasypayService = apiClient) : ViewModel() {

    private val _state = MutableStateFlow<LoginScreenUiState>(LoginScreenUiState.Start)
    val state = _state.asStateFlow()

    var isError by mutableStateOf(false)

    private var authJob: Job? = null

    fun resetState() {
        viewModelScope.launch {
            _state.value = LoginScreenUiState.Start
            isError = false
        }
    }

    fun getMessage(): String = with(state.value) {
        when (this) {
            is LoginScreenUiState.Error -> "Error: $msg"
            is LoginScreenUiState.Exception -> "Exception: ${e.message ?: "Unknown error"}"
            else -> "Exception: Unknown error!"
        }
    }


    fun authorize(creds: Credentials) {

        authJob?.cancel()
        authJob = viewModelScope.launch {
            _state.value = LoginScreenUiState.Loading
            val res = try {
                when (val answer = withContext(Dispatchers.IO) { api.authorize(creds) }) {
                    is ServerLoginResponse.Success -> LoginScreenUiState.Success(answer.response.token)
                    is ServerLoginResponse.Error -> LoginScreenUiState.Error(
                        answer.error.code, answer.error.error
                    ).also { isError = true }
                }
            } catch (e: Throwable) {
                LoginScreenUiState.Exception(e).also { isError = true }
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