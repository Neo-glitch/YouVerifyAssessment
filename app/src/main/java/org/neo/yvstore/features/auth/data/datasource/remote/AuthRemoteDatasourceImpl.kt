package org.neo.yvstore.features.auth.data.datasource.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.neo.yvstore.features.auth.data.datasource.remote.model.AuthUser
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserSignUpRequest

/**
 * Implementation of AuthRemoteDatasource using Firebase Auth and Firestore.
 *
 * @property auth FirebaseAuth instance for authentication
 * @property firestore FirebaseFirestore instance for user profile storage
 */
class AuthRemoteDatasourceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRemoteDatasource {

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        // create a new user with Firebase Auth
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid
            ?: throw IllegalStateException("User creation succeeded but UID is null")

        // Store user profile in Firestore
        val userSignUpRequest = UserSignUpRequest(
            uid = uid,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
        firestore.collection("users")
            .document(uid)
            .set(userSignUpRequest)
            .await()

        authResult.user?.sendEmailVerification()?.await()

        // Sign out to prevent auto-login
        auth.signOut()
    }

    override suspend fun signIn(email: String, password: String): AuthUser {
        // Authenticate with Firebase Auth
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid
            ?: throw IllegalStateException("Sign in succeeded but UID is null")

        // Retrieve user profile from Firestore
        val documentSnapshot = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        if (!documentSnapshot.exists()) {
            throw IllegalStateException("User profile not found in Firestore for UID: $uid")
        }

        return documentSnapshot.toObject(AuthUser::class.java)
            ?: throw IllegalStateException("Failed to deserialize user profile for UID: $uid")
    }
}