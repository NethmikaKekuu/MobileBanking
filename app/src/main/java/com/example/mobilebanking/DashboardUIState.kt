package com.example.mobilebanking

data class DashboardUIState(
    val balance: String = "LKR 125,000.00",
    val isLoading: Boolean = false,
    val greeting: String = "",
)
