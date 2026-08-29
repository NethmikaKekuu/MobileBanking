package com.example.mobilebanking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class DashboardFragment : Fragment() {

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
    }
}
