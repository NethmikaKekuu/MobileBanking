package com.example.mobilebanking

data class TransferRequest(
    val recipientAccount: String,
    val recipientName: String,
    val amount: Double,
    val remarks: String,
    val id: String = "",
) : java.io.Serializable
