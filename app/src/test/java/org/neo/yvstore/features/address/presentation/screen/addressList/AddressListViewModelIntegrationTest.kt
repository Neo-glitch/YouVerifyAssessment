package org.neo.yvstore.features.address.presentation.screen.addressList

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.usecase.DeleteAddressUseCase
import org.neo.yvstore.features.address.domain.usecase.GetAddressesUseCase
import org.neo.yvstore.features.address.domain.usecase.RefreshAddressesUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestAddressRepository

class AddressListViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createTestAddresses() = listOf(
        Address(
            id = "a1",
            userId = "u1",
            streetAddress = "123 Main St",
            city = "New York",
            state = "NY",
            country = "USA"
        ),
        Address(
            id = "a2",
            userId = "u1",
            streetAddress = "456 Oak Ave",
            city = "Los Angeles",
            state = "CA",
            country = "USA"
        )
    )

    @Test
    fun `init should load cached addresses and set Loaded state`() = runTest {
        // Arrange - emit addresses BEFORE constructing ViewModel
        val testAddressRepository = TestAddressRepository()
        testAddressRepository.refreshResult = Resource.Success(Unit)
        testAddressRepository.emit(createTestAddresses())

        // Act - construct ViewModel (init{} runs here)
        val viewModel = AddressListViewModel(
            getAddressesUseCase = GetAddressesUseCase(testAddressRepository),
            deleteAddressUseCase = DeleteAddressUseCase(testAddressRepository),
            refreshAddressesUseCase = RefreshAddressesUseCase(testAddressRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(AddressListLoadState.Loaded)
        assertThat(state.addresses).hasSize(2)
        assertThat(state.addresses[0].streetAddress).isEqualTo("123 Main St")
        assertThat(state.addresses[1].streetAddress).isEqualTo("456 Oak Ave")
    }

    @Test
    fun `init should show Error when cache empty and refresh fails`() = runTest {
        // Arrange - no addresses, refresh fails
        val testAddressRepository = TestAddressRepository()
        testAddressRepository.refreshResult = Resource.Error("Network error")

        // Act - construct ViewModel
        val viewModel = AddressListViewModel(
            getAddressesUseCase = GetAddressesUseCase(testAddressRepository),
            deleteAddressUseCase = DeleteAddressUseCase(testAddressRepository),
            refreshAddressesUseCase = RefreshAddressesUseCase(testAddressRepository)
        )

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(AddressListLoadState.Error::class.java)
        assertThat((state.loadState as AddressListLoadState.Error).message).isEqualTo("Network error")
        assertThat(state.addresses).isEmpty()
    }

    @Test
    fun `init should send toast when refresh fails but cached addresses exist`() = runTest {
        // Arrange - emit addresses but set refresh to fail
        val testAddressRepository = TestAddressRepository()
        testAddressRepository.refreshResult = Resource.Error("Failed to refresh")
        testAddressRepository.emit(createTestAddresses())

        // Act - construct ViewModel
        val viewModel = AddressListViewModel(
            getAddressesUseCase = GetAddressesUseCase(testAddressRepository),
            deleteAddressUseCase = DeleteAddressUseCase(testAddressRepository),
            refreshAddressesUseCase = RefreshAddressesUseCase(testAddressRepository)
        )

        // Assert
        viewModel.uiEvent.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(AddressListUiEvent.ShowToast::class.java)
            assertThat((event as AddressListUiEvent.ShowToast).message).isEqualTo("Failed to refresh")
            cancelAndConsumeRemainingEvents()
        }

        // Addresses should still be shown
        assertThat(viewModel.uiState.value.addresses).hasSize(2)
    }

    @Test
    fun `onDeleteAddress should optimistically remove then confirm`() = runTest {
        // Arrange
        val testAddressRepository = TestAddressRepository()
        testAddressRepository.refreshResult = Resource.Success(Unit)
        testAddressRepository.deleteResult = Resource.Success(Unit)
        testAddressRepository.emit(createTestAddresses())

        val viewModel = AddressListViewModel(
            getAddressesUseCase = GetAddressesUseCase(testAddressRepository),
            deleteAddressUseCase = DeleteAddressUseCase(testAddressRepository),
            refreshAddressesUseCase = RefreshAddressesUseCase(testAddressRepository)
        )

        val addressToDelete = viewModel.uiState.value.addresses[0]

        // Act
        viewModel.onDeleteAddress(addressToDelete)

        // Assert - address should be removed
        assertThat(viewModel.uiState.value.addresses).hasSize(1)
        assertThat(viewModel.uiState.value.addresses[0].id).isEqualTo("a2")
        assertThat(viewModel.uiState.value.deleteError).isNull()
    }

    @Test
    fun `onDeleteAddress should restore address on failure and set deleteError`() = runTest {
        // Arrange
        val testAddressRepository = TestAddressRepository()
        testAddressRepository.refreshResult = Resource.Success(Unit)
        testAddressRepository.deleteResult = Resource.Error("Failed to delete address")
        testAddressRepository.emit(createTestAddresses())

        val viewModel = AddressListViewModel(
            getAddressesUseCase = GetAddressesUseCase(testAddressRepository),
            deleteAddressUseCase = DeleteAddressUseCase(testAddressRepository),
            refreshAddressesUseCase = RefreshAddressesUseCase(testAddressRepository)
        )

        val addressToDelete = viewModel.uiState.value.addresses[0]

        // Act
        viewModel.onDeleteAddress(addressToDelete)

        // Assert - address should be restored and error set
        assertThat(viewModel.uiState.value.addresses).hasSize(2)
        assertThat(viewModel.uiState.value.deleteError).isEqualTo("Failed to delete address")
        assertThat(viewModel.uiState.value.addresses.any { it.id == "a1" }).isTrue()
    }

    @Test
    fun `onDismissDeleteError should clear deleteError`() = runTest {
        // Arrange
        val testAddressRepository = TestAddressRepository()
        testAddressRepository.refreshResult = Resource.Success(Unit)
        testAddressRepository.deleteResult = Resource.Error("Failed to delete")
        testAddressRepository.emit(createTestAddresses())

        val viewModel = AddressListViewModel(
            getAddressesUseCase = GetAddressesUseCase(testAddressRepository),
            deleteAddressUseCase = DeleteAddressUseCase(testAddressRepository),
            refreshAddressesUseCase = RefreshAddressesUseCase(testAddressRepository)
        )

        val addressToDelete = viewModel.uiState.value.addresses[0]
        viewModel.onDeleteAddress(addressToDelete)
        assertThat(viewModel.uiState.value.deleteError).isNotNull()

        // Act
        viewModel.onDismissDeleteError()

        // Assert
        assertThat(viewModel.uiState.value.deleteError).isNull()
    }
}
