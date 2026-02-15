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
            loadCachedAddresses()
            observeAddresses()
            refreshAddresses()
        }
    }

    private suspend fun loadCachedAddresses() {
        val initialResult = getAddressesUseCase().first()
        initialResult.onSuccess { addresses ->
            if (addresses.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        addresses = addresses.map { address -> address.toAddressUi() },
                        loadState = AddressListLoadState.Loaded,
                    )
                }
            }
        }.onError { error ->
            _uiState.update {
                it.copy(loadState = AddressListLoadState.Error(error))
            }
        }
    }

    private fun observeAddresses() {
        viewModelScope.launch {
            getAddressesUseCase().collect { resource ->
                resource.onSuccess { addresses ->
                    val allAddresses = addresses.map { address -> address.toAddressUi() }
                    val pending = pendingDeleteAddress
                    _uiState.update {
                        it.copy(
                            addresses = if (pending != null) {
                                allAddresses.filter { address -> address.id != pending.id }
                            } else {
                                allAddresses
                            },
                        )
                    }
                }
            }
        }
    }

    private suspend fun refreshAddresses() {
        val result = refreshAddressesUseCase()
        result.onSuccess {
            _uiState.update {
                it.copy(loadState = AddressListLoadState.Loaded)
            }
        }.onError { message ->
            val currentState = _uiState.value
            if (currentState.addresses.isEmpty() && currentState.loadState !is AddressListLoadState.Error) {
                _uiState.update {
                    it.copy(loadState = AddressListLoadState.Error(message))
                }
            } else if (currentState.addresses.isNotEmpty()) {
                _uiEvent.send(AddressListUiEvent.ShowToast(message))
            }
        }
    }

    fun onDeleteAddress(address: AddressUi) {
        pendingDeleteAddress = address
        _uiState.update {
            it.copy(
                addresses = it.addresses.filter { item -> item.id != address.id },
                deleteError = null
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
                        deleteError = message
                    )
                }
            }
        }
    }

    fun onDismissDeleteError() {
        _uiState.update { it.copy(deleteError = null) }
    }
}
