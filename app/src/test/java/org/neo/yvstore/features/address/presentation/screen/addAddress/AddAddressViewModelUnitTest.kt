package org.neo.yvstore.features.address.presentation.screen.addAddress

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
import org.neo.yvstore.features.address.domain.usecase.AddAddressUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class AddAddressViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val addAddressUseCase: AddAddressUseCase = mockk()
    private lateinit var viewModel: AddAddressViewModel

    @Before
    fun setUp() {
        viewModel = AddAddressViewModel(addAddressUseCase)
    }

    private fun fillValidForm() {
        viewModel.onStreetAddressChange("123 Main St")
        viewModel.onStreetAddressBlur()
        viewModel.onCityChange("Springfield")
        viewModel.onCityBlur()
        viewModel.onStateChange("Illinois")
        viewModel.onStateBlur()
        viewModel.onCountryChange("USA")
        viewModel.onCountryBlur()
    }

    @Test
    fun `onSave success emits SaveSuccess event`() = runTest {
        coEvery { addAddressUseCase(any()) } returns Resource.Success(Unit)

        fillValidForm()

        viewModel.uiEvent.test {
            viewModel.onSave()
            val event = awaitItem()
            assertThat(event).isInstanceOf(AddAddressUiEvent.SaveSuccess::class.java)
        }
    }

    @Test
    fun `onSave error updates load state`() = runTest {
        coEvery { addAddressUseCase(any()) } returns Resource.Error("User not found")

        fillValidForm()
        viewModel.onSave()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(AddAddressLoadStateState.Error::class.java)
        assertThat((state.loadState as AddAddressLoadStateState.Error).message).isEqualTo("User not found")
    }

    @Test
    fun `onSave with empty fields shows validation errors`() {
        viewModel.onSave()

        val state = viewModel.uiState.value
        assertThat(state.streetAddress.errorMsg).isEqualTo("Street address is required")
        assertThat(state.city.errorMsg).isEqualTo("City is required")
        assertThat(state.state.errorMsg).isEqualTo("State is required")
        assertThat(state.country.errorMsg).isEqualTo("Country is required")
    }

    @Test
    fun `dismissError resets load state`() = runTest {
        coEvery { addAddressUseCase(any()) } returns Resource.Error("error")

        fillValidForm()
        viewModel.onSave()
        viewModel.dismissError()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(AddAddressLoadStateState.Idle)
    }

    @Test
    fun `onStreetAddressChange updates value`() {
        viewModel.onStreetAddressChange("456 Oak Ave")

        assertThat(viewModel.uiState.value.streetAddress.value).isEqualTo("456 Oak Ave")
        assertThat(viewModel.uiState.value.streetAddress.hasBeenModified).isTrue()
    }
}
