package org.neo.yvstore.features.auth.presentation.screens.signup

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.auth.domain.usecase.SignUpUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestAuthRepository

class SignUpViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SignUpViewModel
    private lateinit var testAuthRepository: TestAuthRepository

    @Before
    fun setup() {
        testAuthRepository = TestAuthRepository()
        val signUpUseCase = SignUpUseCase(testAuthRepository)
        viewModel = SignUpViewModel(signUpUseCase)
    }

    @Test
    fun `signUp should emit Success event when all inputs valid`() = runTest {
        // Arrange
        testAuthRepository.signUpResult = Resource.Success(Unit)

        viewModel.onEmailChange("test@example.com")
        viewModel.onEmailBlur()
        viewModel.onFirstNameChange("John")
        viewModel.onFirstNameBlur()
        viewModel.onLastNameChange("Doe")
        viewModel.onLastNameBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onConfirmPasswordBlur()

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.signUp()

            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpEvent.Success::class.java)
            assertThat((event as SignUpEvent.Success).message).isEqualTo("Account created successfully.")

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `signUp should set Error loadState when signUp fails`() = runTest {
        // Arrange
        testAuthRepository.signUpResult = Resource.Error("Email already in use")

        viewModel.onEmailChange("test@example.com")
        viewModel.onEmailBlur()
        viewModel.onFirstNameChange("John")
        viewModel.onFirstNameBlur()
        viewModel.onLastNameChange("Doe")
        viewModel.onLastNameBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onConfirmPasswordBlur()

        // Act
        viewModel.signUp()

        // Assert
        val loadState = viewModel.uiState.value.loadState
        assertThat(loadState).isInstanceOf(SignUpLoadState.Error::class.java)
        assertThat((loadState as SignUpLoadState.Error).message).isEqualTo("Email already in use")
    }

    @Test
    fun `signUp should not proceed when inputs are invalid`() = runTest {
        // Arrange - don't fill in fields
        val initialLoadState = viewModel.uiState.value.loadState

        // Act
        viewModel.signUp()

        // Assert
        assertThat(viewModel.uiState.value.loadState).isEqualTo(initialLoadState)
        assertThat(viewModel.uiState.value.loadState).isEqualTo(SignUpLoadState.Idle)
    }

    @Test
    fun `onPasswordChange should revalidate confirmPassword when already modified`() = runTest {
        // Arrange - set and blur confirmPassword first
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onConfirmPasswordBlur()

        // Act - change password to something different
        viewModel.onPasswordChange("different456")

        // Assert - confirmPassword should now have an error
        assertThat(viewModel.uiState.value.confirmPassword.errorMsg).isNotNull()
        assertThat(viewModel.uiState.value.confirmPassword.errorMsg).isEqualTo("Passwords do not match")
    }

    @Test
    fun `dismissError should reset loadState to Idle`() = runTest {
        // Arrange
        testAuthRepository.signUpResult = Resource.Error("Test error")
        viewModel.onEmailChange("test@example.com")
        viewModel.onEmailBlur()
        viewModel.onFirstNameChange("John")
        viewModel.onFirstNameBlur()
        viewModel.onLastNameChange("Doe")
        viewModel.onLastNameBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onConfirmPasswordBlur()
        viewModel.signUp()

        // Act
        viewModel.dismissError()

        // Assert
        assertThat(viewModel.uiState.value.loadState).isEqualTo(SignUpLoadState.Idle)
    }
}
