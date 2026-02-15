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

class AddAddressUseCaseUnitTest {

    private val repository: AddressRepository = mockk()
    private lateinit var useCase: AddAddressUseCase

    @Before
    fun setUp() {
        useCase = AddAddressUseCase(repository)
    }

    @Test
    fun `invoke should return success from repository`() = runTest {
        val address = Address("", "", "123 Main", "City", "State", "Country")
        coEvery { repository.addAddress(address) } returns Resource.Success(Unit)

        val result = useCase(address)

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        coVerify(exactly = 1) { repository.addAddress(address) }
    }

    @Test
    fun `invoke should return error from repository`() = runTest {
        val address = Address("", "", "123 Main", "City", "State", "Country")
        coEvery { repository.addAddress(address) } returns Resource.Error("User not found")

        val result = useCase(address)

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("User not found")
    }
}
