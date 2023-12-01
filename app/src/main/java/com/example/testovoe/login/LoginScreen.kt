package com.example.testovoe.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testovoe.ui.theme.shapes


@Composable
fun LoginScreen(
    onGetToken: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    vm: LoginViewModel = viewModel()
) {
    var login by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val state by vm.state.collectAsState()

    Column {
        LoginField(labelValue = "Login", login = login, onLoginChanged = { login = it })
        PasswordField(
            labelValue = "Parol",
            password = password,
            onPasswordChange = { password = it })
        Button(onClick = { vm.authorize(Credentials(login, password)) }) {
            Text("Войти")
        }
        val context = LocalContext.current
        when (state) {
            LoginScreenUiState.Start -> Unit
            LoginScreenUiState.Loading -> CircularProgressIndicator()
            is LoginScreenUiState.Success -> {
                onGetToken((state as LoginScreenUiState.Success).token)
                vm.resetState()
            }

            is LoginScreenUiState.Error -> {
                showError(context, (state as LoginScreenUiState.Error).msg)
                vm.resetState()
            }

            is LoginScreenUiState.Exception -> {
                showError(
                    context,
                    "Error: ${(state as LoginScreenUiState.Exception).e.message ?: "Unknown Error"}"
                )
                vm.resetState()
            }


        }

    }
}

private fun showError(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

sealed class AuthUiState() {
    data object Wait : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val msg: String) : AuthUiState()

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
        /*colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            backgroundColor = BgColor
        ),*/
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password,
        onValueChange = onPasswordChange,
        /*  leadingIcon = {
              Icon(painter = painterResource, contentDescription = "")
          },*/
        trailingIcon = {

            val iconImage = if (passwordVisible.value) {
                Icons.Filled.ArrowForward
            } else {
                Icons.Filled.ArrowBack
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
