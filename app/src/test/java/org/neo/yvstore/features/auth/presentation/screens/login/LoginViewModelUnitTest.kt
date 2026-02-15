package org.neo.yvstore.features.auth.presentation.screens.login

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.domain.usecase.LoginUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val userManager: UserManager = mockk(relaxUnitFun = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(loginUseCase, userManager)
    }

    @Test
    fun `login success saves user and emits event`() = runTest {
        val user = User("1", "test@test.com", "John", "Doe")
        coEvery { loginUseCase(any(), any()) } returns Resource.Success(user)

        // Set valid inputs
        viewModel.onEmailChange("test@test.com")
        viewModel.onEmailBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()

        viewModel.uiEvent.test {
            viewModel.login()
            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginUiEvent.LoginSuccess::class.java)
        }

        coVerify { userManager.saveUser(user) }
    }

    @Test
    fun `login error updates load state`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Error("Invalid email or password")

        viewModel.onEmailChange("test@test.com")
        viewModel.onEmailBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()

        viewModel.login()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(LoginLoadState.Error::class.java)
        assertThat((state.loadState as LoginLoadState.Error).message).isEqualTo("Invalid email or password")
    }

    @Test
    fun `login does nothing when inputs are invalid`() = runTest {
        viewModel.login()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(LoginLoadState.Idle)
    }

    @Test
    fun `onEmailChange updates email value`() {
        viewModel.onEmailChange("test@test.com")

        assertThat(viewModel.uiState.value.email.value).isEqualTo("test@test.com")
        assertThat(viewModel.uiState.value.email.hasBeenModified).isTrue()
    }

    @Test
    fun `onEmailBlur validates and shows error for invalid email`() {
        viewModel.onEmailChange("invalid")
        viewModel.onEmailBlur()

        assertThat(viewModel.uiState.value.email.hasLostFocus).isTrue()
        assertThat(viewModel.uiState.value.email.errorMsg).isNotNull()
    }

    @Test
    fun `dismissError resets load state to idle`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Resource.Error("error")

        viewModel.onEmailChange("test@test.com")
        viewModel.onEmailBlur()
        viewModel.onPasswordChange("password123")
        viewModel.onPasswordBlur()
        viewModel.login()

        viewModel.dismissError()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(LoginLoadState.Idle)
    }

    @Test
    fun `togglePasswordVisibility toggles state`() {
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()
        viewModel.togglePasswordVisibility()
        assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()
        viewModel.togglePasswordVisibility()
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()
    }
}
