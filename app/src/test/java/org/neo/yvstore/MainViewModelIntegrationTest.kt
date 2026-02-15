package org.neo.yvstore

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.data.UserManagerImpl
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.TestAppCache


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var userManager: UserManagerImpl
    private lateinit var testAppCache: TestAppCache

    @Before
    fun setup() {
        testAppCache = TestAppCache()
        userManager = UserManagerImpl(testAppCache)
    }

    @Test
    fun `init should set AUTHENTICATED when user exists`() = runTest {
        // Arrange - save user before constructing VM
        val testUser = User(
            uid = "test-uid",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )
        userManager.saveUser(testUser)

        // Act
        viewModel = MainViewModel(userManager)
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.authState.value).isEqualTo(AppAuthState.AUTHENTICATED)
    }

    @Test
    fun `init should set UNAUTHENTICATED when no user`() = runTest {

        // Act
        viewModel = MainViewModel(userManager)
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.authState.value).isEqualTo(AppAuthState.UNAUTHENTICATED)
    }
}
