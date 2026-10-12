package com.example.mobilebanking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    goToMain()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        this@LoginActivity, errString, Toast.LENGTH_SHORT
                    ).show()
                    showLoginForm()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        this@LoginActivity,
                        "Fingerprint not recognized, try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Sign in to Banking App")
            .setSubtitle("Use your fingerprint or face to continue")
            .setNegativeButtonText("Use password instead")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
            .build()

        // A fingerprint only unlocks a Firebase session that already exists on this device.
        // The first sign-in always goes through email and password.
        if (AuthRepository().currentUser != null && canUseBiometrics()) {
            setContentView(R.layout.activity_login)
            biometricPrompt.authenticate(promptInfo)
        } else {
            showLoginForm()
        }
    }

    private fun canUseBiometrics(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val result = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        )
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun showLoginForm() {
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    LaunchedEffect(uiState.isSignedIn) {
                        if (uiState.isSignedIn) goToMain()
                    }
                    LoginScreen(
                        uiState = uiState,
                        onEmailChange = viewModel::onEmailChange,
                        onPasswordChange = viewModel::onPasswordChange,
                        onSignIn = viewModel::onSignIn,
                        onRegister = viewModel::onRegister,
                    )
                }
            }
        })
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
