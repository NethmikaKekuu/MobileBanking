package com.example.mobilebanking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(
    private val auth: AuthRepository = AuthRepository(),
    private val transfers: TransferRepository = TransferRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardUIState(userEmail = auth.currentUser?.email.orEmpty())
    )
    val uiState: StateFlow<DashboardUIState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true, greeting = greetingForNow()) }

        // There is no real data source yet, so the short delay stands in for loading the balance.
        viewModelScope.launch {
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    // Signing out leaves Firestore's offline cache on the phone, so it is cleared before
    // going back to the login screen. Otherwise the next user could see this user's transfers.
    fun onSignOut(onDone: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()
            runCatching { transfers.clearLocalCache() } // sign-out must finish even if this fails
            onDone()
        }
    }

    private fun greetingForNow(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}
