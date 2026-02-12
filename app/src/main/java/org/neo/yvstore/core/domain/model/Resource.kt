package org.neo.yvstore.core.domain.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()

    inline fun onSuccess(block: (T) -> Unit): Resource<T> {
        if (this is Success) block(data)
        return this
    }

    inline fun onError(block: (String) -> Unit): Resource<T> {
        if (this is Error) block(message)
        return this
    }

    fun isSuccess(): Boolean = this is Success

    fun isError(): Boolean = this is Error

    fun dataOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    fun errorOrNull(): String? = when (this) {
        is Error -> message
        is Success -> null
    }
}

