package com.example.mobilebanking

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class DashboardFragment : Fragment() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startSessionReminder()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnGoToTransfer).setOnClickListener {
            (requireActivity() as MainActivity).showTransferFragment()
        }

        view.findViewById<Button>(R.id.btnGoToHistory).setOnClickListener {
            (requireActivity() as MainActivity).showHistoryFragment()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            startSessionReminder()
        }
    }

    private fun startSessionReminder() {
        requireContext().startService(
            Intent(requireContext(), SessionReminderService::class.java)
        )
    }
}
