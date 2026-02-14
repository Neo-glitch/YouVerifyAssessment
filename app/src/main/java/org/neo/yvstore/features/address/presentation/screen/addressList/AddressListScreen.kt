package org.neo.yvstore.features.address.presentation.screen.addressList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.neo.yvstore.features.address.presentation.model.AddressUi
import org.neo.yvstore.features.address.presentation.screen.addressList.components.AddressItemCard

@Composable
fun AddressListScreen(
    onNavigateBack: () -> Unit,
    onAddAddress: () -> Unit,
    viewModel: AddressListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddressListScreen(
        addresses = uiState.addresses,
        loadState = uiState.loadState,
        deleteError = uiState.deleteError,
        onNavigateBack = onNavigateBack,
        onAddAddress = onAddAddress,
        onDeleteAddress = viewModel::onDeleteAddress,
        onDismissDeleteError = viewModel::onDismissDeleteError
    )
}

@Composable
private fun AddressListScreen(
    addresses: List<AddressUi>,
    loadState: AddressListLoadState,
    deleteError: String?,
    onNavigateBack: () -> Unit,
    onAddAddress: () -> Unit,
    onDeleteAddress: (AddressUi) -> Unit,
    onDismissDeleteError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    DeleteErrorEffect(
        deleteError = deleteError,
        snackbarHostState = snackbarHostState,
        onDismiss = onDismissDeleteError
    )

    Box(modifier = Modifier.fillMaxSize()) {
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
                    visible = loadState is AddressListLoadState.Loaded && addresses.isNotEmpty(),
                    onClick = onAddAddress
                )
            }
        ) { paddingValues ->
            AddressListContent(
                addresses = addresses,
                loadState = loadState,
                onAddAddress = onAddAddress,
                onDeleteAddress = onDeleteAddress,
                paddingValues = paddingValues
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun DeleteErrorEffect(
    deleteError: String?,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit
) {
    LaunchedEffect(deleteError) {
        if (deleteError != null) {
            snackbarHostState.showSnackbar(deleteError)
            onDismiss()
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
private fun AddressListContent(
    addresses: List<AddressUi>,
    loadState: AddressListLoadState,
    onAddAddress: () -> Unit,
    onDeleteAddress: (AddressUi) -> Unit,
    paddingValues: PaddingValues
) {
    when (loadState) {
        is AddressListLoadState.Loading -> {
            AddressListLoadingContent(modifier = Modifier.padding(paddingValues))
        }

        is AddressListLoadState.Error -> {
            AddressListErrorContent(
                message = loadState.message,
                modifier = Modifier.padding(paddingValues)
            )
        }

        is AddressListLoadState.Loaded -> {
            if (addresses.isEmpty()) {
                AddressListEmptyContent(
                    onAddAddress = onAddAddress,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                AddressListPopulatedContent(
                    addresses = addresses,
                    onDeleteAddress = onDeleteAddress,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun AddressListLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        YVStoreCircleProgressIndicator(size = 48.dp)
    }
}

@Composable
private fun AddressListErrorContent(
    message: String,
    modifier: Modifier = Modifier
) {
    YVStoreEmptyErrorStateView(
        image = R.drawable.ic_empty_cart,
        title = "Error loading addresses",
        description = message,
        modifier = modifier
    )
}

@Composable
private fun AddressListEmptyContent(
    onAddAddress: () -> Unit,
    modifier: Modifier = Modifier
) {
    YVStoreEmptyErrorStateView(
        image = R.drawable.ic_empty_cart,
        title = "No addresses yet",
        description = "Add a delivery address to continue",
        actionButtonText = "Add Address",
        onActionButtonClick = onAddAddress,
        modifier = modifier
    )
}

@Composable
private fun AddressListPopulatedContent(
    addresses: List<AddressUi>,
    onDeleteAddress: (AddressUi) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = addresses,
            key = { it.id }
        ) { address ->
            AddressItemCard(
                address = address,
                onDelete = { onDeleteAddress(address) }
            )
            Spacer(modifier = Modifier.height(12.dp))
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
            deleteError = null,
            onNavigateBack = {},
            onAddAddress = {},
            onDeleteAddress = {},
            onDismissDeleteError = {},
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
            deleteError = null,
            onNavigateBack = {},
            onAddAddress = {},
            onDeleteAddress = {},
            onDismissDeleteError = {},
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
            deleteError = null,
            onNavigateBack = {},
            onAddAddress = {},
            onDeleteAddress = {},
            onDismissDeleteError = {},
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
            deleteError = null,
            onNavigateBack = {},
            onAddAddress = {},
            onDeleteAddress = {},
            onDismissDeleteError = {},
        )
    }
}
