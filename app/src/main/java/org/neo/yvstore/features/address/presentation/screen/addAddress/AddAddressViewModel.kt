package org.neo.yvstore.features.address.presentation.screen.addAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.features.address.domain.usecase.AddAddressUseCase

class AddAddressViewModel(
    private val addAddressUseCase: AddAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddAddressUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onStreetAddressChange(value: String) {
        _uiState.update {
            val current = it.streetAddress
            it.copy(
                streetAddress = current.copy(
                    value = value,
                    hasBeenModified = true,
                    errorMsg = if (current.hasLostFocus) validateRequired(value, "Street address") else null,
                ),
            )
        }
    }

    fun onStreetAddressBlur() {
        if (!_uiState.value.streetAddress.hasBeenModified) return
        _uiState.update {
            it.copy(
                streetAddress = it.streetAddress.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.streetAddress.value, "Street address"),
                ),
            )
        }
    }

    fun onCityChange(value: String) {
        _uiState.update {
            val current = it.city
            it.copy(
                city = current.copy(
                    value = value,
                    hasBeenModified = true,
                    errorMsg = if (current.hasLostFocus) validateRequired(value, "City") else null,
                ),
            )
        }
    }

    fun onCityBlur() {
        if (!_uiState.value.city.hasBeenModified) return
        _uiState.update {
            it.copy(
                city = it.city.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.city.value, "City"),
                ),
            )
        }
    }

    fun onStateChange(value: String) {
        _uiState.update {
            val current = it.state
            it.copy(
                state = current.copy(
                    value = value,
                    hasBeenModified = true,
                    errorMsg = if (current.hasLostFocus) validateRequired(value, "State") else null,
                ),
            )
        }
    }

    fun onStateBlur() {
        if (!_uiState.value.state.hasBeenModified) return
        _uiState.update {
            it.copy(
                state = it.state.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.state.value, "State"),
                ),
            )
        }
    }

    fun onCountryChange(value: String) {
        _uiState.update {
            val current = it.country
            it.copy(
                country = current.copy(
                    value = value,
                    hasBeenModified = true,
                    errorMsg = if (current.hasLostFocus) validateRequired(value, "Country") else null,
                ),
            )
        }
    }

    fun onCountryBlur() {
        if (!_uiState.value.country.hasBeenModified) return
        _uiState.update {
            it.copy(
                country = it.country.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.country.value, "Country"),
                ),
            )
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(loadState = AddAddressLoadStateState.Idle) }
    }

    fun onSave() {
        _uiState.update {
            it.copy(
                streetAddress = it.streetAddress.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.streetAddress.value, "Street address"),
                ),
                city = it.city.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.city.value, "City"),
                ),
                state = it.state.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.state.value, "State"),
                ),
                country = it.country.copy(
                    hasLostFocus = true,
                    errorMsg = validateRequired(it.country.value, "Country"),
                ),
            )
        }

        if (!_uiState.value.isFormValid) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = AddAddressLoadStateState.Saving) }

            val address = Address(
                id = "",
                userId = "",
                streetAddress = state.streetAddress.value,
                city = state.city.value,
                state = state.state.value,
                country = state.country.value
            )

            val result = addAddressUseCase(address)
            result.onSuccess {
                _uiEvent.send(AddAddressUiEvent.SaveSuccess)
            }.onError { message ->
                _uiState.update {
                    it.copy(loadState = AddAddressLoadStateState.Error(message))
                }
            }
        }
    }

    private fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName is required" else null
    }
}
