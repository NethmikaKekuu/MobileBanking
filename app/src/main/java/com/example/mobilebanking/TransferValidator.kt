package com.example.mobilebanking

object TransferValidator {

    const val MAX_AMOUNT = 500000.0

    /**
     * Returns null if the input is valid, or an error message naming
     * the first problem found, in the same order onSubmitTransfer()
     * checked them.
     */
    fun validate(
        account: String,
        name: String,
        amountText: String
    ): String? {
        if (account.isBlank()) {
            return "Enter a recipient account number"
        }

        if (name.isBlank()) {
            return "Enter a recipient name"
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            return "Enter a valid amount greater than 0"
        }

        if (amount > MAX_AMOUNT) {
            return "Maximum transfer amount is LKR 500,000"
        }

        return null
    }
}
