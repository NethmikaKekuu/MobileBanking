package com.example.mobilebanking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ConfirmationFragment : Fragment() {

    companion object {
        const val ARG_TRANSFER_REQUEST = "transfer_request"
    }

    private lateinit var transferRequest: TransferRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferRequest = BundleCompat.getSerializable(
            requireArguments(),
            ARG_TRANSFER_REQUEST,
            TransferRequest::class.java
        )!!

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-LK"))
        val formattedAmount = currencyFormat.format(transferRequest.amount).replace("LKR", "LKR ")

        view.findViewById<TextView>(R.id.tvConfirmRecipient).text =
            getString(R.string.label_recipient_format, transferRequest.recipientName, transferRequest.recipientAccount)
        view.findViewById<TextView>(R.id.tvConfirmAmount).text = formattedAmount
        view.findViewById<TextView>(R.id.tvConfirmRemarks).text =
            transferRequest.remarks.ifEmpty { getString(R.string.no_remarks) }

        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            lifecycleScope.launch {
                AppDatabase.getInstance(requireContext()).transferDao().insert(transferRequest)
                Toast.makeText(requireContext(), getString(R.string.msg_transfer_submitted), Toast.LENGTH_LONG).show()
                (requireActivity() as MainActivity).showDashboardFragment()
            }
        }

        view.findViewById<Button>(R.id.btnEditTransfer).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
