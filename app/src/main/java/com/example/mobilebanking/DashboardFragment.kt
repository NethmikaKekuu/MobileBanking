package com.example.mobilebanking

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startSessionReminder()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    DashboardScreen(
                        uiState = uiState,
                        onGoToTransfer = {
                            (requireActivity() as MainActivity).showTransferFragment()
                        },
                        onGoToHistory = {
                            (requireActivity() as MainActivity).showHistoryFragment()
                        },
                        onSignOut = {
                            // Application context, because the fragment may be gone by the time
                            // the cache has been cleared. CLEAR_TASK stops Back returning here.
                            val appContext = requireContext().applicationContext
                            viewModel.onSignOut {
                                appContext.startActivity(
                                    Intent(appContext, LoginActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                            }
                        },
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
