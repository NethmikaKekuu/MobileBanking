package com.example.mobilebanking

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth = Firebase.auth) {

    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun signOut() = auth.signOut()
}
