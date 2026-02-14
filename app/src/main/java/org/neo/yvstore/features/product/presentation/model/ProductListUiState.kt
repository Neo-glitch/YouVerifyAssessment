package org.neo.yvstore.features.product.presentation.model

data class ProductListUiState(
    val products: List<ProductItemUi> = emptyList(),
    val loadState: ProductListLoadState = ProductListLoadState.Loading,
)
