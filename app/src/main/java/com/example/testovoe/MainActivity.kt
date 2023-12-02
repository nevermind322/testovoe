package com.example.testovoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.testovoe.login.LoginScreen
import com.example.testovoe.payments.PaymentsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    var token by remember { mutableStateOf<String?>(null) }
    if (token == null)
        LoginScreen({ token = it })
    else
        PaymentsScreen(token = token!!, { token = null })
}
