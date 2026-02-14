package org.neo.yvstore.features.address.presentation.screen.addressList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.ui.component.card.YVStoreElevatedCard
import org.neo.yvstore.core.ui.component.navigation.YVStoreTopBar
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator
import org.neo.yvstore.core.ui.component.status.YVStoreEmptyErrorStateView
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.features.address.presentation.model.AddressUi

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

    LaunchedEffect(deleteError) {
        if (deleteError != null) {
            snackbarHostState.showSnackbar(deleteError)
            onDismissDeleteError()
        }
    }

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
            if (loadState is AddressListLoadState.Loaded && addresses.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddAddress,
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
    ) { paddingValues ->
        when (loadState) {
            is AddressListLoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    YVStoreCircleProgressIndicator(size = 48.dp)
                }
            }
            is AddressListLoadState.Error -> {
                YVStoreEmptyErrorStateView(
                    image = R.drawable.ic_empty_cart,
                    title = "Error loading addresses",
                    description = loadState.message,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is AddressListLoadState.Loaded -> {
                if (addresses.isEmpty()) {
                    YVStoreEmptyErrorStateView(
                        image = R.drawable.ic_empty_cart,
                        title = "No addresses yet",
                        description = "Add a delivery address to continue",
                        actionButtonText = "Add Address",
                        onActionButtonClick = onAddAddress,
                        modifier = Modifier.padding(paddingValues)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(
                            items = addresses,
                            key = { it.id }
                        ) { address ->
                            AddressCard(
                                address = address,
                                onDelete = { onDeleteAddress(address) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
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
private fun AddressCard(
    address: AddressUi,
    onDelete: () -> Unit
) {
    YVStoreElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = address.streetAddress,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${address.city}, ${address.state}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = address.country,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Delete address",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

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
