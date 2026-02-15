package org.neo.yvstore.features.address.presentation.screen.addressList

import io.mockk.coEvery
import io.mockk.coVerify
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
import org.neo.yvstore.features.address.presentation.model.AddressUi
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
    fun `init loads addresses and sets loaded state`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(listOf(address)))
        coEvery { refreshAddressesUseCase() } returns Resource.Success(Unit)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.addresses).hasSize(1)
        assertThat(state.loadState).isEqualTo(AddressListLoadState.Loaded)
    }

    @Test
    fun `init with empty cache and refresh error shows error`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(emptyList()))
        coEvery { refreshAddressesUseCase() } returns Resource.Error("User not found")

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.loadState).isInstanceOf(AddressListLoadState.Error::class.java)
    }

    @Test
    fun `onDeleteAddress optimistically removes address from list`() = runTest {
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
    fun `onDeleteAddress rolls back on error`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(listOf(address)))
        coEvery { refreshAddressesUseCase() } returns Resource.Success(Unit)
        coEvery { deleteAddressUseCase("a1") } returns Resource.Error("Network error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val addressUi = viewModel.uiState.value.addresses[0]
        viewModel.onDeleteAddress(addressUi)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.addresses).hasSize(1)
        assertThat(viewModel.uiState.value.deleteError).isEqualTo("Network error")
    }

    @Test
    fun `onDismissDeleteError clears error`() = runTest {
        every { getAddressesUseCase() } returns flowOf(Resource.Success(listOf(address)))
        coEvery { refreshAddressesUseCase() } returns Resource.Success(Unit)
        coEvery { deleteAddressUseCase("a1") } returns Resource.Error("Network error")

        val viewModel = createViewModel()
        advanceUntilIdle()

        val addressUi = viewModel.uiState.value.addresses[0]
        viewModel.onDeleteAddress(addressUi)
        advanceUntilIdle()

        viewModel.onDismissDeleteError()

        assertThat(viewModel.uiState.value.deleteError).isNull()
    }
}
