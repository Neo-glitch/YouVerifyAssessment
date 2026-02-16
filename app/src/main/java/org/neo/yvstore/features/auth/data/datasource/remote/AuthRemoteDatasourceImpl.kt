package org.neo.yvstore.features.auth.data.datasource.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.neo.yvstore.core.common.dispatcher.DispatcherProvider
import org.neo.yvstore.core.network.utils.awaitWithTimeout
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserSignUpRequest

class AuthRemoteDatasourceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider
) : AuthRemoteDatasource {

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        // create a new user with Firebase Auth
        val authResult = auth.createUserWithEmailAndPassword(email, password)
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)
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
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)

        auth.signOut()
    }

    override suspend fun signIn(email: String, password: String): UserDto {
        // Authenticate with Firebase Auth
        val authResult = auth.signInWithEmailAndPassword(email, password)
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)
        val uid = authResult.user?.uid
            ?: throw IllegalStateException("Sign in succeeded but UID is null")

        // Retrieve user profile from Firestore
        val documentSnapshot = firestore.collection("users")
            .document(uid)
            .get()
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)

        if (!documentSnapshot.exists()) {
            throw IllegalStateException("User profile not found in Firestore for UID: $uid")
        }

        return documentSnapshot.toObject(UserDto::class.java)
            ?: throw IllegalStateException("Failed to deserialize user profile for UID: $uid")
    }
}
