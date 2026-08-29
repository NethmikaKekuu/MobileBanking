package com.example.mobilebanking

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transfer_history")
data class TransferRequest(
    val recipientAccount: String,
    val recipientName: String,
    val amount: Double,
    val remarks: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : java.io.Serializable
