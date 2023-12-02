package com.example.testovoe.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testovoe.ui.theme.shapes


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onGetToken: (String) -> Unit, vm: LoginViewModel = viewModel()
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val state by vm.state.collectAsState()

    LaunchedEffect(vm.isError) {
        if (vm.isError) {
            snackbarHostState.showSnackbar(vm.getMessage())
            vm.resetState()
        }
    }

    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 8.dp)
            ) {
                LoginField(labelValue = "Login", login = login, onLoginChanged = { login = it })
                PasswordField(labelValue = "Password",
                    password = password,
                    onPasswordChange = { password = it })
                Button(onClick = {
                    keyboard?.hide()
                    vm.authorize(Credentials(login, password))
                }) {
                    Text("Войти")
                }
                when (state) {
                    LoginScreenUiState.Start -> Unit
                    LoginScreenUiState.Loading -> Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) { CircularProgressIndicator() }

                    is LoginScreenUiState.Success -> {
                        onGetToken((state as LoginScreenUiState.Success).token)
                        vm.resetState()
                    }

                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun LoginField(
    labelValue: String,
    login: String,
    onLoginChanged: (String) -> Unit,
) {

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shapes.small),
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = login,
        onValueChange = onLoginChanged,
    )
}

@Composable
fun PasswordField(
    labelValue: String,
    password: String,
    onPasswordChange: (String) -> Unit,
) {

    val localFocusManager = LocalFocusManager.current
    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shapes.small),
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password,
        onValueChange = onPasswordChange,
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                "спрятать пароль"
            } else {
                "показать пароль"
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }

        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
    )
}
