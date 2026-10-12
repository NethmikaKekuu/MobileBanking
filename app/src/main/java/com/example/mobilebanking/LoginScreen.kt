package com.example.mobilebanking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    uiState: LoginUIState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onRegister: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // On Android 15 and up the app draws behind the status bar, so keep the form clear of it.
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Sign in to Banking App", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
        uiState.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = onSignIn, modifier = Modifier.fillMaxWidth()) { Text("Sign in") }
            TextButton(onClick = onRegister) { Text("Create account") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            uiState = LoginUIState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSignIn = {},
            onRegister = {},
        )
    }
}
