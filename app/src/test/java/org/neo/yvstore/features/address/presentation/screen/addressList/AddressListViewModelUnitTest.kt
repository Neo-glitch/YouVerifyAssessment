package org.neo.yvstore.features.address.presentation.screen.addressList

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.usecase.DeleteAddressUseCase
import org.neo.yvstore.features.address.domain.usecase.GetAddressesUseCase
import org.neo.yvstore.features.address.domain.usecase.RefreshAddressesUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class AddressListViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAddressesUseCase: GetAddressesUseCase = mockk()
    private val deleteAddressUseCase: DeleteAddressUseCase = mockk()
    private val refreshAddressesUseCase: RefreshAddressesUseCase = mockk()

    private val address = Address("a1", "u1", "123 Main", "City", "State", "Country")

    private fun createViewModel(): AddressListViewModel {
        return AddressListViewModel(
            getAddressesUseCase,
            deleteAddressUseCase,
            refreshAddressesUseCase
        )
    }

    @Test
    fun `init should load addresses and set loaded state`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(listOf(address)))
        coEvery { refreshAddressesUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.addresses).hasSize(1)
        assertThat(state.loadState).isEqualTo(AddressListLoadState.Loaded)
    }

    @Test
    fun `init with empty cache and refresh error should show error state`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshAddressesUseCase() } returns Resource.Error("User not found")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isInstanceOf(AddressListLoadState.Error::class.java)
    }

    @Test
    fun `onDeleteAddress should optimistically remove address from list`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(listOf(address)))
        coEvery { refreshAddressesUseCase() } returns Resource.Success(Unit)
        coEvery { deleteAddressUseCase("a1") } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val addressUi = viewModel.uiState.value.addresses[0]
        viewModel.onDeleteAddress(addressUi)

        assertThat(viewModel.uiState.value.addresses).isEmpty()
    }

    @Test
    fun `onDeleteAddress should roll back and emit Error event on failure`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(listOf(address)))
        coEvery { refreshAddressesUseCase() } returns Resource.Success(Unit)
        coEvery { deleteAddressUseCase("a1") } returns Resource.Error("Network error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val addressUi = viewModel.uiState.value.addresses[0]
        viewModel.uiEvent.test {
            viewModel.onDeleteAddress(addressUi)
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.addresses).hasSize(1)
            val event = awaitItem()
            assertThat(event).isInstanceOf(AddressListUiEvent.Error::class.java)
            assertThat((event as AddressListUiEvent.Error).message).isEqualTo("Network error")
            cancelAndConsumeRemainingEvents()
        }
    }
}
