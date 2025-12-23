package com.example.myfintech.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.myfintech.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


class GoogleAuthClient(private val context: Context){
    private val auth  = Firebase.auth
    private val credentialManager = CredentialManager.create(context)
    private val TAG = "GoogleAuthClient"


    // Fungsi login baru menggunakan Credential Manager
    // In GoogleAuthClient.kt

    suspend fun signIn(activityContext: Context): Pair<Boolean, String?> {
        try {
            // Use the passed-in activity context to get the string
            val webClientId = activityContext.getString(R.string.default_web_client_id)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Use the passed-in activity context here as well
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext // <-- Use activityContext here
            )

            val credential = result.credential
            if (credential is GoogleIdTokenCredential) {
                val googleIdToken = credential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                auth.signInWithCredential(firebaseCredential).await()

                Log.d(TAG, "Firebase sign in successful")
                return Pair(true, null)
            } else {
                Log.e(TAG, "Unexpected credential type")
                return Pair(false, "Unexpected credential type")
            }

        } catch (e: GetCredentialException) {
            Log.e(TAG, "Credential Manager Error", e)
            // Provide a more user-friendly message for a common cancellation scenario
            if (e is androidx.credentials.exceptions.NoCredentialException) {
                return Pair(false, "User cancelled the sign-in process.")
            }
            return Pair(false, e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Unknown Sign In Error", e)
            return Pair(false, e.message)
        }
    }


    fun getCurrentUser() = auth.currentUser

    fun signOut() {
        auth.signOut()
    }
}