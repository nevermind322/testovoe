package com.example.testovoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.testovoe.api.ServerAnswer
import com.example.testovoe.api.apiClient
import com.example.testovoe.login.Credentials
import com.example.testovoe.login.LoginScreen
import com.example.testovoe.payments.PaymentsScreen
import com.example.testovoe.payments.PaymentsViewModel
import com.example.testovoe.ui.theme.AppTheme
import kotlinx.coroutines.launch

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

    if (token == null) LoginScreen({ token = it })
    else PaymentsScreen(token = token!!, { token = null })
}
