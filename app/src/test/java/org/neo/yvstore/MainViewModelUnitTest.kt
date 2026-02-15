package org.neo.yvstore

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userManager: UserManager = mockk()

    @Test
    fun `initial state is LOADING`() {
        every { userManager.observeUser() } returns flowOf()
        val viewModel = MainViewModel(userManager)

        assertThat(viewModel.authState.value).isEqualTo(AppAuthState.LOADING)
    }

    @Test
    fun `authenticated user sets AUTHENTICATED state`() = runTest {
        val user = User("1", "a@b.com", "John", "Doe")
        every { userManager.observeUser() } returns flowOf(user)

        val viewModel = MainViewModel(userManager)
        advanceUntilIdle()

        assertThat(viewModel.authState.value).isEqualTo(AppAuthState.AUTHENTICATED)
    }

    @Test
    fun `null user sets UNAUTHENTICATED state`() = runTest {
        every { userManager.observeUser() } returns flowOf(null)

        val viewModel = MainViewModel(userManager)
        advanceUntilIdle()

        assertThat(viewModel.authState.value).isEqualTo(AppAuthState.UNAUTHENTICATED)
    }
}
