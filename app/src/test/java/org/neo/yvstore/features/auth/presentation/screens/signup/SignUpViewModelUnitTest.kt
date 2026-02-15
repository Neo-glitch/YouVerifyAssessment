package org.neo.yvstore.features.auth.presentation.screens.signup

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.auth.domain.usecase.SignUpUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val signUpUseCase: SignUpUseCase = mockk()
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        viewModel = SignUpViewModel(signUpUseCase)
    }

    private fun fillValidForm() {
        viewModel.onEmailChange("test@test.com")
        viewModel.onEmailBlur()
        viewModel.onFirstNameChange("John")
        viewModel.onFirstNameBlur()
        viewModel.onLastNameChange("Doe")
        viewModel.onLastNameBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onConfirmPasswordBlur()
    }

    @Test
    fun `signUp should emit success event on success`() = runTest {
        coEvery { signUpUseCase(any(), any(), any(), any()) } returns Resource.Success(Unit)

        fillValidForm()

        viewModel.uiEvent.test {
            viewModel.signUp()
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpEvent.Success::class.java)
        }
    }

    @Test
    fun `signUp should update load state on error`() = runTest {
        coEvery { signUpUseCase(any(), any(), any(), any()) } returns Resource.Error("Email in use")

        fillValidForm()
        viewModel.signUp()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(SignUpLoadState.Error::class.java)
        assertThat((state.loadState as SignUpLoadState.Error).message).isEqualTo("Email in use")
    }

    @Test
    fun `signUp should do nothing when inputs are invalid`() = runTest {
        viewModel.signUp()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(SignUpLoadState.Idle)
    }

    @Test
    fun `password mismatch should show error on confirm password`() {
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.onConfirmPasswordChange("different")
        viewModel.onConfirmPasswordBlur()

        assertThat(viewModel.uiState.value.confirmPassword.errorMsg).isEqualTo("Passwords do not match")
    }

    @Test
    fun `dismissError should reset load state`() = runTest {
        coEvery { signUpUseCase(any(), any(), any(), any()) } returns Resource.Error("error")

        fillValidForm()
        viewModel.signUp()
        viewModel.dismissError()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(SignUpLoadState.Idle)
    }

    @Test
    fun `togglePasswordVisibility should toggle state`() {
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()
        viewModel.togglePasswordVisibility()
        assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()
    }

    @Test
    fun `toggleConfirmPasswordVisibility should toggle state`() {
        assertThat(viewModel.uiState.value.isConfirmPasswordVisible).isFalse()
        viewModel.toggleConfirmPasswordVisibility()
        assertThat(viewModel.uiState.value.isConfirmPasswordVisible).isTrue()
    }
}
