package org.neo.yvstore.features.address.presentation.screen.addAddress

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.usecase.AddAddressUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestAddressRepository


class AddAddressViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AddAddressViewModel
    private lateinit var testAddressRepository: TestAddressRepository

    @Before
    fun setup() {
        testAddressRepository = TestAddressRepository()
        val addAddressUseCase = AddAddressUseCase(testAddressRepository)
        viewModel = AddAddressViewModel(addAddressUseCase)
    }

    @Test
    fun `onSave should emit SaveSuccess when all fields valid`() = runTest {
        // Arrange
        testAddressRepository.addResult = Resource.Success(Unit)

        viewModel.onStreetAddressChange("123 Main St")
        viewModel.onStreetAddressBlur()
        viewModel.onCityChange("New York")
        viewModel.onCityBlur()
        viewModel.onStateChange("NY")
        viewModel.onStateBlur()
        viewModel.onCountryChange("USA")
        viewModel.onCountryBlur()

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.onSave()

            val event = awaitItem()
            assertThat(event).isEqualTo(AddAddressUiEvent.SaveSuccess)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSave should show validation errors when fields blank`() = runTest {
        // Arrange - don't fill in fields

        // Act
        viewModel.onSave()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.streetAddress.errorMsg).isNotNull()
        assertThat(state.streetAddress.errorMsg).isEqualTo("Street address is required")
        assertThat(state.city.errorMsg).isNotNull()
        assertThat(state.city.errorMsg).isEqualTo("City is required")
        assertThat(state.state.errorMsg).isNotNull()
        assertThat(state.state.errorMsg).isEqualTo("State is required")
        assertThat(state.country.errorMsg).isNotNull()
        assertThat(state.country.errorMsg).isEqualTo("Country is required")
    }

    @Test
    fun `onSave should set Error loadState when use case fails`() = runTest {
        // Arrange
        testAddressRepository.addResult = Resource.Error("Failed to save address")

        viewModel.onStreetAddressChange("123 Main St")
        viewModel.onStreetAddressBlur()
        viewModel.onCityChange("New York")
        viewModel.onCityBlur()
        viewModel.onStateChange("NY")
        viewModel.onStateBlur()
        viewModel.onCountryChange("USA")
        viewModel.onCountryBlur()

        // Act
        viewModel.onSave()

        // Assert
        val loadState = viewModel.uiState.value.loadState
        assertThat(loadState).isInstanceOf(AddAddressLoadStateState.Error::class.java)
        assertThat((loadState as AddAddressLoadStateState.Error).message).isEqualTo("Failed to save address")
    }

    @Test
    fun `dismissError should reset loadState to Idle`() = runTest {
        // Arrange
        testAddressRepository.addResult = Resource.Error("Test error")
        viewModel.onStreetAddressChange("123 Main St")
        viewModel.onStreetAddressBlur()
        viewModel.onCityChange("New York")
        viewModel.onCityBlur()
        viewModel.onStateChange("NY")
        viewModel.onStateBlur()
        viewModel.onCountryChange("USA")
        viewModel.onCountryBlur()
        viewModel.onSave()

        // Act
        viewModel.dismissError()

        // Assert
        assertThat(viewModel.uiState.value.loadState).isEqualTo(AddAddressLoadStateState.Idle)
    }
}
