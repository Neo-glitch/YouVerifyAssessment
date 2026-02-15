package org.neo.yvstore.features.address.presentation.screen.addressList

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.features.address.presentation.model.AddressUi
import org.neo.yvstore.features.address.presentation.screen.addressList.components.AddressItem

@Composable
fun AddressListScreen(
    onNavigateBack: () -> Unit,
    onAddAddress: () -> Unit,
    onAddressSelected: (String) -> Unit = {},
    viewModel: AddressListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is AddressListUiEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    AddressListScreen(
        addresses = uiState.addresses,
        loadState = uiState.loadState,
        onNavigateBack = onNavigateBack,
        onAddAddress = onAddAddress,
        onAddressSelected = onAddressSelected,
        onDeleteAddress = viewModel::onDeleteAddress
    )
}

@Composable
private fun AddressListScreen(
    addresses: List<AddressUi>,
    loadState: AddressListLoadState,
    onNavigateBack: () -> Unit,
    onAddAddress: () -> Unit,
    onAddressSelected: (String) -> Unit,
    onDeleteAddress: (AddressUi) -> Unit
) {
    YVStoreScaffold(
        topBar = {
            YVStoreTopBar(
                title = "Delivery Address",
                onNavigationClick = onNavigateBack,
                isCenteredAligned = true
            )
        },
        floatingActionButton = {
            AddAddressFab(
                visible = addresses.isNotEmpty(),
                onClick = onAddAddress
            )
        }
    ) { paddingValues ->
        if (addresses.isEmpty()) {
            EmptyStateContent(
                loadState = loadState,
                onAddAddress = onAddAddress,
                paddingValues = paddingValues
            )
        } else {
            AddressListLoadedContent(
                addresses = addresses,
                onAddressSelected = onAddressSelected,
                onDeleteAddress = onDeleteAddress,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun AddAddressFab(
    visible: Boolean,
    onClick: () -> Unit
) {
    if (visible) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add address",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun EmptyStateContent(
    loadState: AddressListLoadState,
    onAddAddress: () -> Unit,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        when (loadState) {
            is AddressListLoadState.Loading -> {
                YVStoreCircleProgressIndicator(size = 48.dp)
            }

            is AddressListLoadState.Error -> {
                YVStoreEmptyErrorStateView(
                    image = R.drawable.ic_error,
                    title = "Error loading addresses",
                    description = loadState.message,
                )
            }

            is AddressListLoadState.Loaded -> {
                YVStoreEmptyErrorStateView(
                    image = R.drawable.ic_empty_address,
                    title = "No addresses yet",
                    description = "Add a delivery address to continue",
                    actionButtonText = "Add Address",
                    onActionButtonClick = onAddAddress,
                )
            }
        }
    }
}

@Composable
private fun AddressListLoadedContent(
    addresses: List<AddressUi>,
    onAddressSelected: (String) -> Unit,
    onDeleteAddress: (AddressUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Select delivery address",
            style = MaterialTheme.typography.titleMedium,
            color = YVStoreTheme.colors.textColors.textPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp, bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = addresses,
                key = { it.id }
            ) { address ->
                AddressItem(
                    address = address,
                    onClick = { onAddressSelected(address.id) },
                    onDelete = { onDeleteAddress(address) }
                )
            }
        }
    }
}


// Preview
private val sampleAddresses = listOf(
    AddressUi(
        id = "1",
        streetAddress = "123 Main Street",
        city = "San Francisco",
        state = "California",
        country = "United States",
        formattedAddress = "123 Main Street, San Francisco, California, United States"
    ),
    AddressUi(
        id = "2",
        streetAddress = "456 Oak Avenue",
        city = "Los Angeles",
        state = "California",
        country = "United States",
        formattedAddress = "456 Oak Avenue, Los Angeles, California, United States"
    ),
    AddressUi(
        id = "3",
        streetAddress = "789 Pine Road",
        city = "Seattle",
        state = "Washington",
        country = "United States",
        formattedAddress = "789 Pine Road, Seattle, Washington, United States"
    ),
)

@Preview(showBackground = true)
@Composable
private fun AddressListScreenPopulatedPreview() {
    YVStoreTheme {
        AddressListScreen(
            addresses = sampleAddresses,
            loadState = AddressListLoadState.Loaded,
            onNavigateBack = {},
            onAddAddress = {},
            onAddressSelected = {},
            onDeleteAddress = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressListScreenEmptyPreview() {
    YVStoreTheme {
        AddressListScreen(
            addresses = emptyList(),
            loadState = AddressListLoadState.Loaded,
            onNavigateBack = {},
            onAddAddress = {},
            onAddressSelected = {},
            onDeleteAddress = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressListScreenLoadingPreview() {
    YVStoreTheme {
        AddressListScreen(
            addresses = emptyList(),
            loadState = AddressListLoadState.Loading,
            onNavigateBack = {},
            onAddAddress = {},
            onAddressSelected = {},
            onDeleteAddress = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressListScreenErrorPreview() {
    YVStoreTheme {
        AddressListScreen(
            addresses = emptyList(),
            loadState = AddressListLoadState.Error("Failed to load addresses"),
            onNavigateBack = {},
            onAddAddress = {},
            onAddressSelected = {},
            onDeleteAddress = {},
        )
    }
}
