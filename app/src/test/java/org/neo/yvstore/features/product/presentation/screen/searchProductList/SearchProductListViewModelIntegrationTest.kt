package org.neo.yvstore.features.product.presentation.screen.searchProductList

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.SearchProductsUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import org.neo.yvstore.testdoubles.repository.TestProductRepository

class SearchProductListViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchProductListViewModel
    private lateinit var testProductRepository: TestProductRepository

    @Before
    fun setup() {
        testProductRepository = TestProductRepository()
        val searchProductsUseCase = SearchProductsUseCase(testProductRepository)
        viewModel = SearchProductListViewModel(searchProductsUseCase)
    }

    private fun createTestProducts() = listOf(
        Product(
            id = "p1",
            name = "Laptop",
            description = "Gaming laptop",
            price = 999.99,
            imageUrl = "url1",
            rating = 4.5,
            reviewCount = 100,
            createdAt = "2024-01-01"
        ),
        Product(
            id = "p2",
            name = "Laptop Stand",
            description = "Adjustable laptop stand",
            price = 29.99,
            imageUrl = "url2",
            rating = 4.0,
            reviewCount = 50,
            createdAt = "2024-01-02"
        )
    )

    @Test
    fun `onSearch should set Loaded with products when results found`() = runTest {
        // Arrange
        val products = createTestProducts()
        testProductRepository.searchResult = Resource.Success(products)
        viewModel.onQueryChanged("laptop")

        // Act
        viewModel.onSearch()

        // Assert
        val loadState = viewModel.uiState.value.loadState
        assertThat(loadState).isInstanceOf(SearchProductListLoadState.Loaded::class.java)
        val loadedProducts = (loadState as SearchProductListLoadState.Loaded).products
        assertThat(loadedProducts).hasSize(2)
        assertThat(loadedProducts[0].name).isEqualTo("Laptop")
    }

    @Test
    fun `onSearch should set Empty when no results found`() = runTest {
        // Arrange
        testProductRepository.searchResult = Resource.Success(emptyList())
        viewModel.onQueryChanged("nonexistent")

        // Act
        viewModel.onSearch()

        // Assert
        val loadState = viewModel.uiState.value.loadState
        assertThat(loadState).isInstanceOf(SearchProductListLoadState.Empty::class.java)
        assertThat((loadState as SearchProductListLoadState.Empty).query).isEqualTo("nonexistent")
    }

    @Test
    fun `onSearch should set Error when search fails`() = runTest {
        // Arrange
        testProductRepository.searchResult = Resource.Error("Search failed")
        viewModel.onQueryChanged("laptop")

        // Act
        viewModel.onSearch()

        // Assert
        val loadState = viewModel.uiState.value.loadState
        assertThat(loadState).isInstanceOf(SearchProductListLoadState.Error::class.java)
        assertThat((loadState as SearchProductListLoadState.Error).message).isEqualTo("Search failed")
    }

    @Test
    fun `onQueryChanged should reset to Idle when query emptied`() = runTest {
        // Arrange - perform a search first
        testProductRepository.searchResult = Resource.Success(createTestProducts())
        viewModel.onQueryChanged("laptop")
        viewModel.onSearch()

        // Act - clear the query
        viewModel.onQueryChanged("")

        // Assert
        assertThat(viewModel.uiState.value.loadState).isEqualTo(SearchProductListLoadState.Idle)
        assertThat(viewModel.uiState.value.query).isEmpty()
    }
}
