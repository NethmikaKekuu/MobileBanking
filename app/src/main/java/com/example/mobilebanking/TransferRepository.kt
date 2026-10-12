package com.example.mobilebanking

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TransferRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val db: FirebaseFirestore = Firebase.firestore,
) {

    private fun transfers() = db.collection("users")
        .document(requireNotNull(auth.currentUser) { "No signed-in user" }.uid)
        .collection("transfers")

    // Does not wait for the write: Firestore saves to its local cache first and syncs later,
    // so waiting here would freeze the screen when the phone is offline.
    fun addTransfer(request: TransferRequest) {
        val data = hashMapOf(
            "recipientAccount" to request.recipientAccount,
            "recipientName" to request.recipientName,
            "amount" to request.amount,
            "remarks" to request.remarks,
            "createdAt" to FieldValue.serverTimestamp(),
        )
        transfers().add(data)
            .addOnFailureListener { e -> Log.w("TransferRepository", "Write failed", e) }
    }

    fun observeTransfers(): Flow<List<TransferRequest>> = callbackFlow {
        val registration = transfers()
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.map { doc ->
                    TransferRequest(
                        recipientAccount = doc.getString("recipientAccount").orEmpty(),
                        recipientName = doc.getString("recipientName").orEmpty(),
                        amount = doc.getDouble("amount") ?: 0.0,
                        remarks = doc.getString("remarks").orEmpty(),
                        id = doc.id,
                    )
                }.orEmpty()
                trySend(items)
            }
        awaitClose { registration.remove() }
    }

    // clearPersistence() only works once Firestore has been stopped, so terminate() goes first.
    suspend fun clearLocalCache() {
        db.terminate().await()
        db.clearPersistence().await()
    }
}
