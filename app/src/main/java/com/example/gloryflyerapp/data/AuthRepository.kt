package com.example.gloryflyerapp.data

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore
    private val TAG = "AuthRepository"

    suspend fun signInWithPhoneNumber(
        phoneNumber: String,
        onVerificationCodeSent: (String) -> Unit,
        onVerificationFailed: (Exception) -> Unit
    ) {
        try {
            Log.d(TAG, "Starting phone authentication for: $phoneNumber")
            
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "Auto-verification completed")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "Verification failed: ${e.message}", e)
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d(TAG, "Verification code sent successfully")
                    onVerificationCodeSent(verificationId)
                }
            }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build()

            Log.d(TAG, "Initiating phone number verification")
            PhoneAuthProvider.verifyPhoneNumber(options)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in signInWithPhoneNumber: ${e.message}", e)
            onVerificationFailed(e)
        }
    }

    suspend fun verifyOtp(verificationId: String, otp: String, name: String? = null): Result<FirebaseUser> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            val result = auth.signInWithCredential(credential).await()
            
            // If name is provided (during signup), create user profile
            if (name != null && result.user != null) {
                val userProfile = UserProfile(
                    name = name,
                    email = result.user?.email ?: "",
                    phone = result.user?.phoneNumber ?: ""
                )
                updateUserProfile(userProfile)
            }
            
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateUserProfile(userProfile: UserProfile): Result<Unit> {
        return try {
            val user = getCurrentUser() ?: throw Exception("User not logged in")
            
            // Update Firebase Auth profile
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(userProfile.name)
                .build()
            user.updateProfile(profileUpdates).await()

            // Deprecated: updateEmail is deprecated. Consider using a newer Firebase Auth API or alternative method.
            // user.updateEmail(userProfile.email).await()

            // Store additional user data in Firestore
            firestore.collection("users")
                .document(user.uid)
                .set(userProfile)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}