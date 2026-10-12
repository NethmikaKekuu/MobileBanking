package com.example.mobilebanking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // The Firestore listener is attached when the screen starts and removed when it stops.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                TransferRepository().observeTransfers()
                    .catch { e ->
                        Log.w("HistoryFragment", "Listen failed", e)
                        Toast.makeText(requireContext(), "Could not load history", Toast.LENGTH_SHORT).show()
                    }
                    .collect { transfers ->
                        recyclerView.adapter = TransferHistoryAdapter(transfers)
                    }
            }
        }
    }
}
