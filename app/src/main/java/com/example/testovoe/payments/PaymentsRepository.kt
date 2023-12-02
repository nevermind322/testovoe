package com.example.testovoe.payments

import com.example.testovoe.api.EasypayService
import com.example.testovoe.api.PaymentResponseJson
import com.example.testovoe.api.ServerPaymentsResponse
import com.example.testovoe.api.apiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class PaymentsRepository(private val api: EasypayService = apiClient) {

    suspend fun getPayments(token: String) = withContext(Dispatchers.IO) {
        try {
            when (val res = api.getPayments(token)) {
                is ServerPaymentsResponse.Error -> RepoResult.Error(res.error.code, res.error.error)
                is ServerPaymentsResponse.Success -> RepoResult.Success(res.response.map(::toPayments))
            }
        } catch (e: Throwable) {
            RepoResult.Error(-1, e.message ?: "Unknown error")
        }
    }
}


fun toPayments(apiAnswer: PaymentResponseJson): Payment {
    val id = apiAnswer.id
    val title = apiAnswer.title
    val amount = apiAnswer.amount?.toDoubleOrNull()
    val created = apiAnswer.created?.toLongOrNull()
    val time = if (created != null) Date(created * 1000) else null

    return Payment(id, title, amount, time)
}


sealed class RepoResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : RepoResult<T>()
    data class Error(val code: Int, val msg: String) : RepoResult<Nothing>()
}