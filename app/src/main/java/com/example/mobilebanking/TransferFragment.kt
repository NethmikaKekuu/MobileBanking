package com.example.mobilebanking

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobilebanking.databinding.FragmentTransferBinding

class TransferFragment : Fragment() {

    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        updateSubmitButtonState()
    }

    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSubmitButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etRecipientAccount.addTextChangedListener(textWatcher)
        binding.etRecipientName.addTextChangedListener(textWatcher)
        binding.etAmount.addTextChangedListener(textWatcher)

        binding.btnSubmit.setOnClickListener {
            onSubmitTransfer()
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val prefs = requireContext().getSharedPreferences("banking_app_prefs", Context.MODE_PRIVATE)
        binding.etRecipientAccount.setText(prefs.getString("last_recipient_account", ""))
        binding.etRecipientName.setText(prefs.getString("last_recipient_name", ""))
    }

    private fun updateSubmitButtonState() {
        val account = binding.etRecipientAccount.text.toString().trim()
        val name = binding.etRecipientName.text.toString().trim()
        val amountText = binding.etAmount.text.toString().trim()

        binding.btnSubmit.isEnabled = account.isNotEmpty() && name.isNotEmpty() && amountText.isNotEmpty()
    }

    private fun onSubmitTransfer() {
        val account = binding.etRecipientAccount.text.toString().trim()
        val name = binding.etRecipientName.text.toString().trim()
        val amountText = binding.etAmount.text.toString().trim()
        val remarks = binding.etRemarks.text.toString().trim()

        if (account.isEmpty()) {
            binding.etRecipientAccount.error = getString(R.string.error_empty_account)
            return
        }

        if (name.isEmpty()) {
            binding.etRecipientName.error = getString(R.string.error_empty_name)
            return
        }

        val amount = amountText.toDoubleOrNull()
        if ((amount == null) || (amount <= 0.0)) {
            binding.etAmount.error = getString(R.string.error_invalid_amount)
            return
        }

        if (amount > 500000.0) {
            binding.etAmount.error = getString(R.string.error_max_amount)
            return
        }

        val request = TransferRequest(account, name, amount, remarks)
        (requireActivity() as MainActivity).showConfirmationFragment(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
