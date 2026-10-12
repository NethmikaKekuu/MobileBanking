package com.example.mobilebanking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, errorMessage = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, errorMessage = null) }

    fun onSignIn() = submit { email, password -> repository.signIn(email, password) }

    fun onRegister() = submit { email, password -> repository.register(email, password) }

    private fun submit(action: suspend (String, String) -> Unit) {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.length < 6) {
            _uiState.update {
                it.copy(errorMessage = "Enter an email and a password of at least 6 characters.")
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                action(state.email.trim(), state.password)
                _uiState.update { it.copy(isLoading = false, isSignedIn = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = messageFor(e)) }
            }
        }
    }

    // FirebaseAuthWeakPasswordException is a subclass of FirebaseAuthInvalidCredentialsException,
    // so it has to be checked first.
    private fun messageFor(e: Exception): String = when (e) {
        is FirebaseAuthWeakPasswordException -> "That password is too weak."
        is FirebaseAuthUserCollisionException -> "An account with this email already exists."
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Email or password is incorrect."
        else -> "Something went wrong. Check your connection and try again."
    }
}
