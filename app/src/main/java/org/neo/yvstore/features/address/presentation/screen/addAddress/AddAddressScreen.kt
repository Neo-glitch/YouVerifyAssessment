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
import org.neo.yvstore.core.ui.component.card.BottomFrameCard
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
        },
        bottomBar = {
            BottomFrameCard {
                YVStorePrimaryButton(
                    text = "Save Address",
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    loading = isSaving,
                    enabled = uiState.isFormValid && !isSaving,
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .imePadding()
        ) {
            AddAddressHeader()

            Spacer(modifier = Modifier.height(20.dp))
            StreetAddressField(
                fieldState = uiState.streetAddress,
                onValueChange = onStreetAddressChange,
                onBlur = onStreetAddressBlur,
                enabled = !isSaving,
            )

            Spacer(modifier = Modifier.height(16.dp))
            CityField(
                fieldState = uiState.city,
                onValueChange = onCityChange,
                onBlur = onCityBlur,
                enabled = !isSaving,
            )

            Spacer(modifier = Modifier.height(16.dp))
            StateField(
                fieldState = uiState.state,
                onValueChange = onStateChange,
                onBlur = onStateBlur,
                enabled = !isSaving,
            )

            Spacer(modifier = Modifier.height(16.dp))
            CountryField(
                fieldState = uiState.country,
                onValueChange = onCountryChange,
                onBlur = onCountryBlur,
                isFormValid = uiState.isFormValid,
                isSaving = isSaving,
                onSave = onSave,
            )
        }
    }
}

@Composable
private fun AddAddressHeader() {
    Text(
        text = "Enter delivery address",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StreetAddressField(
    fieldState: TextInputFieldState,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    enabled: Boolean,
) {
    YVStoreTextInput(
        value = fieldState.value,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        placeholder = "Street address",
        label = "Street Address",
        error = fieldState.errorMsg,
        showError = fieldState.errorMsg != null,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Next,
        enabled = enabled
    )
}

@Composable
private fun CityField(
    fieldState: TextInputFieldState,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    enabled: Boolean,
) {
    YVStoreTextInput(
        value = fieldState.value,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        placeholder = "City",
        label = "City",
        error = fieldState.errorMsg,
        showError = fieldState.errorMsg != null,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Next,
        enabled = enabled
    )
}

@Composable
private fun StateField(
    fieldState: TextInputFieldState,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    enabled: Boolean,
) {
    YVStoreTextInput(
        value = fieldState.value,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        placeholder = "State",
        label = "State",
        error = fieldState.errorMsg,
        showError = fieldState.errorMsg != null,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Next,
        enabled = enabled
    )
}

@Composable
private fun CountryField(
    fieldState: TextInputFieldState,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    isFormValid: Boolean,
    isSaving: Boolean,
    onSave: () -> Unit,
) {
    YVStoreTextInput(
        value = fieldState.value,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        placeholder = "Country",
        label = "Country",
        error = fieldState.errorMsg,
        showError = fieldState.errorMsg != null,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Done,
        onDoneClick = { if (isFormValid && !isSaving) onSave() },
        enabled = !isSaving
    )
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
