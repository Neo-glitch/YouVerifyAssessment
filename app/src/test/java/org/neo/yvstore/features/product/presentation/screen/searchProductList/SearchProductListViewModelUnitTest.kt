package org.neo.yvstore.features.product.presentation.screen.searchProductList

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.product.domain.model.Product
import org.neo.yvstore.features.product.domain.usecase.SearchProductsUseCase
import org.neo.yvstore.testUtils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class SearchProductListViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchProductsUseCase: SearchProductsUseCase = mockk()
    private lateinit var viewModel: SearchProductListViewModel

    @Before
    fun setUp() {
        viewModel = SearchProductListViewModel(searchProductsUseCase)
    }

    @Test
    fun `onQueryChanged updates query in state`() {
        viewModel.onQueryChanged("shoes")

        assertThat(viewModel.uiState.value.query).isEqualTo("shoes")
    }

    @Test
    fun `onQueryChanged with empty resets to idle`() {
        viewModel.onQueryChanged("shoes")
        viewModel.onQueryChanged("")

        assertThat(viewModel.uiState.value.loadState).isInstanceOf(SearchProductListLoadState.Idle::class.java)
    }

    @Test
    fun `onSearch with results sets loaded state`() = runTest {
        val products = listOf(
            Product("1", "Shoe", "Desc", 99.0, "url", 4.5, 10, "2024-01-01")
        )
        coEvery { searchProductsUseCase(any()) } returns Resource.Success(products)

        viewModel.onQueryChanged("Shoe")
        viewModel.onSearch()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(SearchProductListLoadState.Loaded::class.java)
        val loaded = state.loadState as SearchProductListLoadState.Loaded
        assertThat(loaded.products).hasSize(1)
        assertThat(loaded.products[0].name).isEqualTo("Shoe")
    }

    @Test
    fun `onSearch with empty results sets empty state`() = runTest {
        coEvery { searchProductsUseCase(any()) } returns Resource.Success(emptyList())

        viewModel.onQueryChanged("xyz")
        viewModel.onSearch()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(SearchProductListLoadState.Empty::class.java)
        assertThat((state.loadState as SearchProductListLoadState.Empty).query).isEqualTo("xyz")
    }

    @Test
    fun `onSearch error sets error state`() = runTest {
        coEvery { searchProductsUseCase(any()) } returns Resource.Error("Network error")

        viewModel.onQueryChanged("test")
        viewModel.onSearch()

        val state = viewModel.uiState.value
        assertThat(state.loadState).isInstanceOf(SearchProductListLoadState.Error::class.java)
        assertThat((state.loadState as SearchProductListLoadState.Error).message).isEqualTo("Network error")
    }
}
