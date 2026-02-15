package org.neo.yvstore.features.address.presentation.screen.addAddress

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
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
    fun `onSave should emit SaveSuccess event on success`() = runTest {
        coEvery { addAddressUseCase(any()) } returns Resource.Success(Unit)

        fillValidForm()

        viewModel.uiEvent.test {
            viewModel.onSave()
            val event = awaitItem()
            assertThat(event).isInstanceOf(AddAddressUiEvent.SaveSuccess::class.java)
        }
    }

    @Test
    fun `onSave should set error load state on failure`() = runTest {
        coEvery { addAddressUseCase(any()) } returns Resource.Error("User not found")

        fillValidForm()
        viewModel.onSave()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(AddAddressLoadStateState.Error::class.java)
        assertThat((state.loadState as AddAddressLoadStateState.Error).message).isEqualTo("User not found")
    }

    @Test
    fun `onSave with empty fields should show validation errors`() {
        viewModel.onSave()

        val state = viewModel.uiState.value
        assertThat(state.streetAddress.errorMsg).isEqualTo("Street address is required")
        assertThat(state.city.errorMsg).isEqualTo("City is required")
        assertThat(state.state.errorMsg).isEqualTo("State is required")
        assertThat(state.country.errorMsg).isEqualTo("Country is required")
    }

    @Test
    fun `dismissError should reset load state to Idle`() = runTest {
        coEvery { addAddressUseCase(any()) } returns Resource.Error("error")

        fillValidForm()
        viewModel.onSave()
        viewModel.dismissError()

        assertThat(viewModel.uiState.value.loadState).isEqualTo(AddAddressLoadStateState.Idle)
    }

    @Test
    fun `onStreetAddressChange should update value and set modified flag`() {
        viewModel.onStreetAddressChange("456 Oak Ave")

        assertThat(viewModel.uiState.value.streetAddress.value).isEqualTo("456 Oak Ave")
        assertThat(viewModel.uiState.value.streetAddress.hasBeenModified).isTrue()
    }

    @Test
    fun `onCityChange should update value and set modified flag`() {
        viewModel.onCityChange("New York")

        assertThat(viewModel.uiState.value.city.value).isEqualTo("New York")
        assertThat(viewModel.uiState.value.city.hasBeenModified).isTrue()
    }

    @Test
    fun `onStateChange should update value and set modified flag`() {
        viewModel.onStateChange("California")

        assertThat(viewModel.uiState.value.state.value).isEqualTo("California")
        assertThat(viewModel.uiState.value.state.hasBeenModified).isTrue()
    }

    @Test
    fun `onCountryChange should update value and set modified flag`() {
        viewModel.onCountryChange("Canada")

        assertThat(viewModel.uiState.value.country.value).isEqualTo("Canada")
        assertThat(viewModel.uiState.value.country.hasBeenModified).isTrue()
    }

    @Test
    fun `onStreetAddressBlur should set error when field is empty and modified`() {
        viewModel.onStreetAddressChange("")
        viewModel.onStreetAddressBlur()

        assertThat(viewModel.uiState.value.streetAddress.errorMsg).isEqualTo("Street address is required")
        assertThat(viewModel.uiState.value.streetAddress.hasLostFocus).isTrue()
    }

    @Test
    fun `onStreetAddressBlur should do nothing when field not modified`() {
        viewModel.onStreetAddressBlur()

        assertThat(viewModel.uiState.value.streetAddress.errorMsg).isNull()
        assertThat(viewModel.uiState.value.streetAddress.hasLostFocus).isFalse()
    }

    @Test
    fun `onCityBlur should set error when field is empty and modified`() {
        viewModel.onCityChange("")
        viewModel.onCityBlur()

        assertThat(viewModel.uiState.value.city.errorMsg).isEqualTo("City is required")
        assertThat(viewModel.uiState.value.city.hasLostFocus).isTrue()
    }

    @Test
    fun `onStateBlur should set error when field is empty and modified`() {
        viewModel.onStateChange("")
        viewModel.onStateBlur()

        assertThat(viewModel.uiState.value.state.errorMsg).isEqualTo("State is required")
        assertThat(viewModel.uiState.value.state.hasLostFocus).isTrue()
    }

    @Test
    fun `onCountryBlur should set error when field is empty and modified`() {
        viewModel.onCountryChange("")
        viewModel.onCountryBlur()

        assertThat(viewModel.uiState.value.country.errorMsg).isEqualTo("Country is required")
        assertThat(viewModel.uiState.value.country.hasLostFocus).isTrue()
    }

    @Test
    fun `onSave should not call useCase when form is invalid`() = runTest {
        viewModel.onSave()

        coVerify(exactly = 0) { addAddressUseCase(any()) }
    }

    @Test
    fun `onSave should set loadState to Saving while saving`() = runTest {
        coEvery { addAddressUseCase(any()) } coAnswers {
            delay(1000)
            Resource.Success(Unit)
        }

        fillValidForm()
        viewModel.onSave()
        
        assertThat(viewModel.uiState.value.loadState).isEqualTo(AddAddressLoadStateState.Saving)
    }
}
