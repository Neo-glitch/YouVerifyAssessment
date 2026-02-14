package org.neo.yvstore.features.address.presentation.screen.addAddress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.dialog.YVStoreErrorDialog
import org.neo.yvstore.core.ui.component.input.YVStoreTextInput
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.model.TextInputFieldState
import org.neo.yvstore.core.ui.util.ObserveAsEvents

@Composable
fun AddAddressScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddAddressViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is AddAddressUiEvent.SaveSuccess -> onNavigateBack()
        }
    }

    if (uiState.loadState is AddAddressLoadStateState.Error) {
        YVStoreErrorDialog(
            title = "Save Failed",
            description = (uiState.loadState as AddAddressLoadStateState.Error).message,
            onDismiss = viewModel::dismissError,
            onPrimaryButtonClick = viewModel::dismissError,
            primaryButtonText = "OK",
        )
    }

    AddAddressScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onStreetAddressChange = viewModel::onStreetAddressChange,
        onStreetAddressBlur = viewModel::onStreetAddressBlur,
        onCityChange = viewModel::onCityChange,
        onCityBlur = viewModel::onCityBlur,
        onStateChange = viewModel::onStateChange,
        onStateBlur = viewModel::onStateBlur,
        onCountryChange = viewModel::onCountryChange,
        onCountryBlur = viewModel::onCountryBlur,
        onSave = viewModel::onSave,
    )
}

@Composable
private fun AddAddressScreen(
    uiState: AddAddressUiState,
    onNavigateBack: () -> Unit,
    onStreetAddressChange: (String) -> Unit,
    onStreetAddressBlur: () -> Unit,
    onCityChange: (String) -> Unit,
    onCityBlur: () -> Unit,
    onStateChange: (String) -> Unit,
    onStateBlur: () -> Unit,
    onCountryChange: (String) -> Unit,
    onCountryBlur: () -> Unit,
    onSave: () -> Unit,
) {
    val isSaving = uiState.loadState is AddAddressLoadStateState.Saving

    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "Add Address",
                onNavigationClick = onNavigateBack,
                isCenteredAligned = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter delivery address",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            YVStoreTextInput(
                value = uiState.streetAddress.value,
                onValueChange = onStreetAddressChange,
                onFocusChange = { isFocused ->
                    if (!isFocused) onStreetAddressBlur()
                },
                placeholder = "Street address",
                label = "Street Address",
                error = uiState.streetAddress.errorMsg,
                showError = uiState.streetAddress.errorMsg != null,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(16.dp))

            YVStoreTextInput(
                value = uiState.city.value,
                onValueChange = onCityChange,
                onFocusChange = { isFocused ->
                    if (!isFocused) onCityBlur()
                },
                placeholder = "City",
                label = "City",
                error = uiState.city.errorMsg,
                showError = uiState.city.errorMsg != null,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(16.dp))

            YVStoreTextInput(
                value = uiState.state.value,
                onValueChange = onStateChange,
                onFocusChange = { isFocused ->
                    if (!isFocused) onStateBlur()
                },
                placeholder = "State",
                label = "State",
                error = uiState.state.errorMsg,
                showError = uiState.state.errorMsg != null,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(16.dp))

            YVStoreTextInput(
                value = uiState.country.value,
                onValueChange = onCountryChange,
                onFocusChange = { isFocused ->
                    if (!isFocused) onCountryBlur()
                },
                placeholder = "Country",
                label = "Country",
                error = uiState.country.errorMsg,
                showError = uiState.country.errorMsg != null,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
                onDoneClick = { if (uiState.isFormValid && !isSaving) onSave() },
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(32.dp))

            YVStorePrimaryButton(
                text = "Save Address",
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                loading = isSaving,
                enabled = uiState.isFormValid && !isSaving,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddAddressScreenEmptyPreview() {
    YVStoreTheme {
        AddAddressScreen(
            uiState = AddAddressUiState(),
            onNavigateBack = {},
            onStreetAddressChange = {},
            onStreetAddressBlur = {},
            onCityChange = {},
            onCityBlur = {},
            onStateChange = {},
            onStateBlur = {},
            onCountryChange = {},
            onCountryBlur = {},
            onSave = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddAddressScreenFilledPreview() {
    YVStoreTheme {
        AddAddressScreen(
            uiState = AddAddressUiState(
                streetAddress = TextInputFieldState(value = "123 Main Street"),
                city = TextInputFieldState(value = "San Francisco"),
                state = TextInputFieldState(value = "California"),
                country = TextInputFieldState(value = "United States"),
            ),
            onNavigateBack = {},
            onStreetAddressChange = {},
            onStreetAddressBlur = {},
            onCityChange = {},
            onCityBlur = {},
            onStateChange = {},
            onStateBlur = {},
            onCountryChange = {},
            onCountryBlur = {},
            onSave = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddAddressScreenValidationErrorsPreview() {
    YVStoreTheme {
        AddAddressScreen(
            uiState = AddAddressUiState(
                streetAddress = TextInputFieldState(
                    value = "",
                    hasLostFocus = true,
                    errorMsg = "Street address is required"
                ),
                city = TextInputFieldState(
                    value = "",
                    hasLostFocus = true,
                    errorMsg = "City is required"
                ),
                state = TextInputFieldState(value = "California"),
                country = TextInputFieldState(
                    value = "",
                    hasLostFocus = true,
                    errorMsg = "Country is required"
                ),
            ),
            onNavigateBack = {},
            onStreetAddressChange = {},
            onStreetAddressBlur = {},
            onCityChange = {},
            onCityBlur = {},
            onStateChange = {},
            onStateBlur = {},
            onCountryChange = {},
            onCountryBlur = {},
            onSave = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddAddressScreenSavingPreview() {
    YVStoreTheme {
        AddAddressScreen(
            uiState = AddAddressUiState(
                streetAddress = TextInputFieldState(value = "123 Main Street"),
                city = TextInputFieldState(value = "San Francisco"),
                state = TextInputFieldState(value = "California"),
                country = TextInputFieldState(value = "United States"),
                loadState = AddAddressLoadStateState.Saving,
            ),
            onNavigateBack = {},
            onStreetAddressChange = {},
            onStreetAddressBlur = {},
            onCityChange = {},
            onCityBlur = {},
            onStateChange = {},
            onStateBlur = {},
            onCountryChange = {},
            onCountryBlur = {},
            onSave = {},
        )
    }
}
