package org.neo.yvstore.features.address.presentation.screen.addressList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.features.address.domain.usecase.DeleteAddressUseCase
import org.neo.yvstore.features.address.domain.usecase.GetAddressesUseCase
import org.neo.yvstore.features.address.domain.usecase.RefreshAddressesUseCase
import org.neo.yvstore.features.address.presentation.model.AddressUi
import org.neo.yvstore.features.address.presentation.model.toAddressUi

class AddressListViewModel(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val refreshAddressesUseCase: RefreshAddressesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressListUiState())
    val uiState: StateFlow<AddressListUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddressListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var pendingDeleteAddress: AddressUi? = null

    init {
        viewModelScope.launch {
            checkLocalAddresses()
            observeAddresses()
            refreshAddresses()
        }
    }

    private suspend fun checkLocalAddresses() {
        val addresses = getAddressesUseCase().first()
        addresses.onSuccess { items ->
            if (items.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        addresses = items.map { address -> address.toAddressUi() },
                        loadState = AddressListLoadState.Loaded
                    )
                }
            }
        }
    }

    private fun observeAddresses() {
        viewModelScope.launch {
            getAddressesUseCase().collect { resource ->
                resource.onSuccess { addresses ->
                    val pending = pendingDeleteAddress
                    val allAddresses = addresses.map { address -> address.toAddressUi() }

                    val filteredAddress = if (pending != null) {
                        allAddresses.filter { address -> address.id != pending.id }
                    } else {
                        allAddresses
                    }

                    _uiState.update {
                        it.copy(addresses = filteredAddress)
                    }
                }
            }
        }
    }

    fun onRefresh() {
        if (_uiState.value.loadState is AddressListLoadState.Loading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            refreshAddressesUseCase()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun refreshAddresses() {
        refreshAddressesUseCase()
            .onSuccess { handleRefreshSuccess() }
            .onError { message -> handleRefreshError(message) }
    }

    private suspend fun handleRefreshSuccess() {
        _uiState.update { it.copy(loadState = AddressListLoadState.Loaded) }
    }

    private suspend fun handleRefreshError(message: String) {
        if (_uiState.value.addresses.isEmpty()) {
            _uiState.update { it.copy(loadState = AddressListLoadState.Error(message)) }
        } else {
            _uiEvent.send(AddressListUiEvent.Error(message))
        }
    }

    fun onDeleteAddress(address: AddressUi) {
        pendingDeleteAddress = address
        _uiState.update {
            it.copy(
                addresses = it.addresses.filter { item -> item.id != address.id },
            )
        }

        viewModelScope.launch {
            val result = deleteAddressUseCase(address.id)
            result.onSuccess {
                pendingDeleteAddress = null
            }.onError { message ->
                pendingDeleteAddress = null
                _uiState.update {
                    it.copy(
                        addresses = (it.addresses + address).sortedBy { item -> item.id },
                    )
                }
                _uiEvent.send(AddressListUiEvent.Error(message))
            }
        }
    }

}
