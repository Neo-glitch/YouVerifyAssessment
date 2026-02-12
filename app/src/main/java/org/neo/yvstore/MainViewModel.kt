package org.neo.yvstore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.neo.yvstore.core.domain.manager.UserManager
import javax.inject.Inject

class MainViewModel (
    private val userManager: UserManager
) : ViewModel() {
    private val _authState = MutableStateFlow(AppAuthState.LOADING)
    val authState: StateFlow<AppAuthState> = _authState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            userManager.observeUser()
                .map { it != null }
                .collectLatest { isLoggedIn ->
                    _authState.value = if (isLoggedIn) {
                        AppAuthState.AUTHENTICATED
                    } else {
                        AppAuthState.UNAUTHENTICATED
                    }
                }
        }
    }
}

enum class AppAuthState {
    LOADING,
    AUTHENTICATED,
    UNAUTHENTICATED
}