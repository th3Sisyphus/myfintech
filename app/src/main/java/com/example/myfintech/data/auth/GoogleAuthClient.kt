package com.example.myfintech.data.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


class GoogleAuthClient(private val context: Context){
    private val auth  = Firebase.auth

    fun getSignInIntent(): Intent{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("731432835676-65n9htvk5q934fip7tgm0g6mhs965egt.apps.googleusercontent.com").requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    suspend fun signInWithIntent(intent: Intent): Boolean {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.await()
            val googleToken = account.idToken

            if (googleToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleToken, null)
                auth.signInWithCredential(firebaseCredential).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun signOut() {
        auth.signOut()
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
    }
}