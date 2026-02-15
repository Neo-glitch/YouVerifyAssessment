package org.neo.yvstore.features.auth.presentation.screens.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.data.UserManagerImpl
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.domain.usecase.LoginUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.TestAppCache
import org.neo.yvstore.testdoubles.repository.TestAuthRepository

class LoginViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var testAuthRepository: TestAuthRepository
    private lateinit var userManager: UserManagerImpl
    private lateinit var testAppCache: TestAppCache

    @Before
    fun setup() {
        testAuthRepository = TestAuthRepository()
        testAppCache = TestAppCache()
        userManager = UserManagerImpl(testAppCache)

        val loginUseCase = LoginUseCase(testAuthRepository)
        viewModel = LoginViewModel(loginUseCase, userManager)
    }

    @Test
    fun `login should save user and emit LoginSuccess when credentials valid`() = runTest {
        // Arrange
        val testUser = User(
            uid = "test-uid",
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )
        testAuthRepository.signInResult = Resource.Success(testUser)

        viewModel.onEmailChange("test@example.com")
        viewModel.onEmailBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.login()

            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginUiEvent.LoginSuccess::class.java)
            assertThat((event as LoginUiEvent.LoginSuccess).message).isEqualTo("Welcome back!")

            // Verify user saved
            val savedUser = userManager.getUser()
            assertThat(savedUser).isEqualTo(testUser)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `login should set Error loadState when login fails`() = runTest {
        // Arrange
        testAuthRepository.signInResult = Resource.Error("Invalid credentials")

        viewModel.onEmailChange("test@example.com")
        viewModel.onEmailBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()

        // Act
        viewModel.login()

        // Assert
        val loadState = viewModel.uiState.value.loadState
        assertThat(loadState).isInstanceOf(LoginLoadState.Error::class.java)
        assertThat((loadState as LoginLoadState.Error).message).isEqualTo("Invalid credentials")
    }

    @Test
    fun `login should not proceed when inputs are invalid`() = runTest {
        // Arrange
        val initialLoadState = viewModel.uiState.value.loadState

        // Act
        viewModel.login()

        // Assert
        assertThat(viewModel.uiState.value.loadState).isEqualTo(initialLoadState)
        assertThat(viewModel.uiState.value.loadState).isEqualTo(LoginLoadState.Idle)
    }

    @Test
    fun `onEmailBlur should show error for invalid email after field modified`() = runTest {
        // Arrange
        viewModel.onEmailChange("bad-email")

        // Act
        viewModel.onEmailBlur()

        // Assert
        assertThat(viewModel.uiState.value.email.errorMsg).isNotNull()
        assertThat(viewModel.uiState.value.email.hasLostFocus).isTrue()
    }

    @Test
    fun `onPasswordBlur should show error for short password after field modified`() = runTest {
        // Arrange
        viewModel.onPasswordChange("ab")

        // Act
        viewModel.onPasswordBlur()

        // Assert
        assertThat(viewModel.uiState.value.password.errorMsg).isNotNull()
        assertThat(viewModel.uiState.value.password.hasLostFocus).isTrue()
    }

    @Test
    fun `dismissError should reset loadState to Idle`() = runTest {
        // Arrange
        testAuthRepository.signInResult = Resource.Error("Test error")
        viewModel.onEmailChange("test@example.com")
        viewModel.onEmailBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.login()

        // Act
        viewModel.dismissError()

        // Assert
        assertThat(viewModel.uiState.value.loadState).isEqualTo(LoginLoadState.Idle)
    }

    @Test
    fun `togglePasswordVisibility should toggle isPasswordVisible`() = runTest {
        // Arrange
        val initial = viewModel.uiState.value.isPasswordVisible

        // Act
        viewModel.togglePasswordVisibility()

        // Assert
        assertThat(viewModel.uiState.value.isPasswordVisible).isEqualTo(!initial)

        // Act again
        viewModel.togglePasswordVisibility()

        // Assert
        assertThat(viewModel.uiState.value.isPasswordVisible).isEqualTo(initial)
    }
}
