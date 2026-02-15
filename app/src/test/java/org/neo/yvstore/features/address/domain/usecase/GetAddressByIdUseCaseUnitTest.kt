package org.neo.yvstore.features.address.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.repository.AddressRepository
import com.google.common.truth.Truth.assertThat

class GetAddressByIdUseCaseUnitTest {

    private val repository: AddressRepository = mockk()
    private lateinit var useCase: GetAddressByIdUseCase

    @Before
    fun setUp() {
        useCase = GetAddressByIdUseCase(repository)
    }

    @Test
    fun `invoke returns success from repository`() = runTest {
        val address = Address("a1", "u1", "123 Main", "City", "State", "Country")
        coEvery { repository.getAddressById("a1") } returns Resource.Success(address)

        val result = useCase(id = "a1")

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(address)
        coVerify(exactly = 1) { repository.getAddressById("a1") }
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        coEvery { repository.getAddressById("a1") } returns Resource.Error("Address not found")

        val result = useCase(id = "a1")

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Address not found")
    }
}
