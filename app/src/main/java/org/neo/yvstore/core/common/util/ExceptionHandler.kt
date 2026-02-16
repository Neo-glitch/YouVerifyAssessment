package org.neo.yvstore.core.common.util

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.cancellation.CancellationException

object ExceptionHandler {

    private const val INVALID_CREDENTIALS_MSG = "Invalid email or password"
    private const val USER_NOT_FOUND_MSG = "Account not found"
    private const val USER_DISABLED_MSG = "This account has been disabled"
    private const val EMAIL_IN_USE_MSG = "An account with this email already exists"
    private const val EMAIL_NOT_VERIFIED_MSG = "Your email address has not been verified. Please check your inbox and verify your email before signing in."
    private const val WEAK_PASSWORD_MSG = "Password is too weak"
    private const val TOO_MANY_REQUESTS_MSG = "Too many attempts. Please try again later"
    private const val RE_AUTH_REQUIRED_MSG = "Please sign in again to continue"
    private const val PERMISSION_DENIED_MSG = "You don't have permission to perform this action"
    private const val DATA_NOT_FOUND_MSG = "The requested data was not found"
    private const val SERVICE_UNAVAILABLE_MSG = "Service unavailable. Please Check your internet and try again"
    private const val NO_INTERNET_MSG = "No internet connection. Please check your network"
    private const val NETWORK_ERROR_MSG = "A network error occurred. Please try again"
    private const val DATABASE_ERROR_MSG = "A local storage error occurred"
    private const val CACHE_ERROR_MSG = "Failed to access cached data"
    private const val REQUEST_TIMEOUT_MSG = "Request timed out. Please try again"
    private const val UNKNOWN_ERROR_MSG = "An unexpected error occurred"

    fun getErrorMessage(throwable: Throwable): String {
        throwable.printStackTrace()
        return when (throwable) {
            is CancellationException -> throw throwable

            // ── Firebase Auth ──
            is FirebaseAuthWeakPasswordException -> throwable.reason ?: WEAK_PASSWORD_MSG
            is FirebaseAuthInvalidCredentialsException -> INVALID_CREDENTIALS_MSG
            is FirebaseAuthInvalidUserException -> when (throwable.errorCode) {
                "ERROR_USER_DISABLED" -> USER_DISABLED_MSG
                else -> USER_NOT_FOUND_MSG
            }
            is FirebaseAuthUserCollisionException -> EMAIL_IN_USE_MSG
            is FirebaseAuthRecentLoginRequiredException -> RE_AUTH_REQUIRED_MSG
            is FirebaseTooManyRequestsException -> TOO_MANY_REQUESTS_MSG
            is FirebaseNetworkException -> NO_INTERNET_MSG

            // ── Firestore ──
            is FirebaseFirestoreException -> getFirestoreErrorMessage(throwable)

            // ── Room / SQLite ──
            is SQLiteConstraintException -> DATABASE_ERROR_MSG
            is SQLiteException -> DATABASE_ERROR_MSG

            // ── Timeout ──
            is TimeoutException -> REQUEST_TIMEOUT_MSG

            // ── Network IO (before generic IOException) ──
            is UnknownHostException -> NO_INTERNET_MSG
            is SocketException -> NETWORK_ERROR_MSG
            is SocketTimeoutException -> REQUEST_TIMEOUT_MSG

            // ── DataStore / General IO ──
            is IOException -> CACHE_ERROR_MSG
            is ExecutionException -> {
                if (throwable.cause is FirebaseFirestoreException) {
                    getFirestoreErrorMessage(throwable.cause as FirebaseFirestoreException)
                } else {
                    UNKNOWN_ERROR_MSG
                }
            }

            else -> UNKNOWN_ERROR_MSG
        }
    }

    private fun getFirestoreErrorMessage(e: FirebaseFirestoreException): String {
        return when (e.code) {
            Code.PERMISSION_DENIED -> PERMISSION_DENIED_MSG
            Code.NOT_FOUND -> DATA_NOT_FOUND_MSG
            Code.UNAVAILABLE -> SERVICE_UNAVAILABLE_MSG
            Code.DEADLINE_EXCEEDED -> NETWORK_ERROR_MSG
            Code.RESOURCE_EXHAUSTED -> TOO_MANY_REQUESTS_MSG
            Code.UNAUTHENTICATED -> RE_AUTH_REQUIRED_MSG
            else -> UNKNOWN_ERROR_MSG
        }
    }
}
