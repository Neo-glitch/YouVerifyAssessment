package org.neo.yvstore.features.address.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.repository.AddressRepository
import com.google.common.truth.Truth.assertThat

class GetAddressesUseCaseUnitTest {

    private val repository: AddressRepository = mockk()
    private lateinit var useCase: GetAddressesUseCase

    @Before
    fun setUp() {
        useCase = GetAddressesUseCase(repository)
    }

    @Test
    fun `invoke should forward exact flow from repository`() {
        val flow = flowOf(Resource.Success(emptyList<Address>()))
        every { repository.getAddresses() } returns flow

        val result = useCase()

        assertThat(result).isSameInstanceAs(flow)
        verify(exactly = 1) { repository.getAddresses() }
    }
}
